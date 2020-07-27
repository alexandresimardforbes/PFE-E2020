// 
// Decompiled by Procyon v0.5.36
// 

package com.amlogic.tvutil;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class TVDBTransformer
{
    public static final int DB_TO_XML = 0;
    public static final int XML_TO_DB = 1;
    private static final String DTD_PATH = "tv_default.dtd";
    private static final String[] feTypes;
    private static final String[] srvTypes;
    private static final String[] vidFmts;
    private static final String[] audFmts;
    private static final String[] mods;
    private static final String[] bandwidths;
    private static final String[] lnbPowers;
    private static final String[] sig22K;
    private static final String[] tonebursts;
    private static final String[] diseqc10s;
    private static final String[] diseqc11s;
    private static final String[] motors;
    private static final String[] ofdmModes;
    private static final String[] atvVideoStds;
    private static final String[] atvAudioStds;
    private static String strMode;
    private static final String TAG = "TVDBTransformer";
    
    private static int stringToValue(final String[] array, final String str) {
        for (int i = 0; i < array.length; ++i) {
            if (array[i].equals(str)) {
                return i;
            }
        }
        return -1;
    }
    
    private static int getIntValue(final Cursor c, final String strVal, final int default_val) {
        final int col = c.getColumnIndex(strVal);
        if (col >= 0) {
            return c.getInt(col);
        }
        return default_val;
    }
    
    private static String getStringValue(final Cursor c, final String strVal, final int default_val) {
        final int col = c.getColumnIndex(strVal);
        if (col >= 0) {
            return Integer.toString(c.getInt(col));
        }
        return Integer.toString(default_val);
    }
    
    private static String getStringValue(final Cursor c, final String strVal, final String default_val) {
        final int col = c.getColumnIndex(strVal);
        if (col >= 0) {
            return c.getString(col);
        }
        return default_val;
    }
    
    private static int getIntAttr(final String strAttr, final int default_val) {
        if (strAttr != null && !strAttr.isEmpty()) {
            return Integer.parseInt(strAttr);
        }
        return default_val;
    }
    
    private static String getStringAttr(final String strAttr, final String default_val) {
        if (strAttr != null && !strAttr.isEmpty()) {
            return strAttr;
        }
        return default_val;
    }
    
    private static void createGroupElements(final SQLiteDatabase db, final Document document, final Element elemParent) {
        final Cursor curGroup = db.rawQuery("select * from grp_table", (String[])null);
        if (curGroup != null) {
            if (curGroup.moveToFirst()) {
                String strPrograms = "";
                do {
                    final Element elemGroup = document.createElement("program_group");
                    elemParent.appendChild(elemGroup);
                    elemGroup.setAttribute("name", getStringValue(curGroup, "name", ""));
                    final int dbGrpId = getIntValue(curGroup, "db_id", -1);
                    final Cursor curGrpMap = db.rawQuery("select * from grp_map_table where db_grp_id=" + dbGrpId, (String[])null);
                    if (curGrpMap != null) {
                        if (curGrpMap.moveToFirst()) {
                            strPrograms = "";
                            do {
                                final int dbSrvId = getIntValue(curGrpMap, "db_srv_id", -1);
                                if (dbSrvId >= 0) {
                                    if (!strPrograms.isEmpty()) {
                                        strPrograms += " ";
                                    }
                                    strPrograms += Integer.toString(dbSrvId);
                                }
                            } while (curGrpMap.moveToNext());
                        }
                        curGrpMap.close();
                    }
                    elemGroup.setAttribute("programs", strPrograms);
                } while (curGroup.moveToNext());
            }
            curGroup.close();
        }
    }
    
    private static void createChannelListElements(final SQLiteDatabase db, final Document document, final Element elemParent) {
        final Cursor curRegion = db.rawQuery("select distinct name from region_table", (String[])null);
        if (curRegion != null) {
            if (curRegion.moveToFirst()) {
                do {
                    final Element elemChannelList = document.createElement("channel_list");
                    elemParent.appendChild(elemChannelList);
                    final String strName = getStringValue(curRegion, "name", "");
                    elemChannelList.setAttribute("name", strName);
                    int mode = getIntValue(curRegion, "fe_type", 0);
                    elemChannelList.setAttribute("fe_type", TVDBTransformer.feTypes[mode]);
                    Log.d("TVDBTransformer", "Loading " + strName + ", mode " + mode);
                    final Cursor curEntry = db.rawQuery("select * from region_table where name='" + strName + "'", (String[])null);
                    if (curEntry != null) {
                        if (curEntry.moveToFirst()) {
                            mode = getIntValue(curEntry, "fe_type", 0);
                            elemChannelList.setAttribute("fe_type", TVDBTransformer.feTypes[mode]);
                            do {
                                final Element elemChannelEntry = document.createElement("channel_entry");
                                elemChannelList.appendChild(elemChannelEntry);
                                elemChannelEntry.setAttribute("frequency", getStringValue(curEntry, "frequency", 0));
                                if (mode == 1) {
                                    elemChannelEntry.setAttribute("modulation", TVDBTransformer.mods[3]);
                                    elemChannelEntry.setAttribute("symbol_rate", Integer.toString(6875000));
                                }
                                else if (mode == 2) {
                                    elemChannelEntry.setAttribute("bandwidth", TVDBTransformer.bandwidths[getIntValue(curEntry, "bandwidth", 0)]);
                                    elemChannelEntry.setAttribute("ofdm_mode", "dvbt");
                                }
                                else if (mode == 3) {
                                    elemChannelEntry.setAttribute("modulation", TVDBTransformer.mods[7]);
                                }
                                else {
                                    if (mode != 5) {
                                        continue;
                                    }
                                    elemChannelEntry.setAttribute("bandwidth", TVDBTransformer.bandwidths[getIntValue(curEntry, "bandwidth", 0)]);
                                }
                            } while (curEntry.moveToNext());
                        }
                        curEntry.close();
                    }
                } while (curRegion.moveToNext());
            }
            curRegion.close();
        }
    }
    
    private static void createProgramElements(final SQLiteDatabase db, final Document document, final Element elemTP, final int chanID) {
        final Cursor curProgram = db.rawQuery("select * from srv_table where db_ts_id=" + chanID, (String[])null);
        if (curProgram != null) {
            if (curProgram.moveToFirst()) {
                do {
                    final Element elemProg = document.createElement("program");
                    elemTP.appendChild(elemProg);
                    elemProg.setAttribute("name", getStringValue(curProgram, "name", ""));
                    elemProg.setAttribute("service_id", getStringValue(curProgram, "service_id", 65535));
                    elemProg.setAttribute("channel_number", getStringValue(curProgram, "chan_num", 0));
                    int typeTmp = getIntValue(curProgram, "service_type", 0);
                    if (typeTmp > TVDBTransformer.srvTypes.length - 1 || typeTmp < 0) {
                        typeTmp = 0;
                    }
                    elemProg.setAttribute("type", TVDBTransformer.srvTypes[typeTmp]);
                    elemProg.setAttribute("scrambled", (getIntValue(curProgram, "scrambled_flag", 0) == 0) ? "false" : "true");
                    elemProg.setAttribute("parental_lock", (getIntValue(curProgram, "lock", 0) == 0) ? "false" : "true");
                    elemProg.setAttribute("skip", (getIntValue(curProgram, "skip", 0) == 0) ? "false" : "true");
                    elemProg.setAttribute("id", getStringValue(curProgram, "db_id", -1));
                    elemProg.setAttribute("encrypt", (getIntValue(curProgram, "encrypt", 0) == 0) ? "false" : "true");
                    elemProg.setAttribute("major_chan_num", getStringValue(curProgram, "major_chan_num", 0));
                    elemProg.setAttribute("minor_chan_num", getStringValue(curProgram, "minor_chan_num", 0));
                    final int vidPid = getIntValue(curProgram, "vid_pid", 8191);
                    final int vidFmt = getIntValue(curProgram, "vid_fmt", 0);
                    if (vidPid < 8191 && vidFmt >= 0 && vidFmt < TVDBTransformer.vidFmts.length) {
                        final Element elemVid = document.createElement("video");
                        elemProg.appendChild(elemVid);
                        elemVid.setAttribute("pid", Integer.toString(vidPid));
                        elemVid.setAttribute("format", TVDBTransformer.vidFmts[vidFmt]);
                    }
                    final String apids = getStringValue(curProgram, "aud_pids", "");
                    final String afmts = getStringValue(curProgram, "aud_fmts", "");
                    final String alangs = getStringValue(curProgram, "aud_langs", "");
                    if (!apids.isEmpty()) {
                        final String[] pids = apids.split(" ");
                        final String[] fmts = afmts.split(" ");
                        final String[] langs = alangs.split(" ");
                        if (pids.length <= 0) {
                            continue;
                        }
                        for (int i = 0; i < pids.length; ++i) {
                            final Element elemAud = document.createElement("audio");
                            elemProg.appendChild(elemAud);
                            elemAud.setAttribute("pid", pids[i]);
                            elemAud.setAttribute("format", TVDBTransformer.audFmts[Integer.parseInt(fmts[i])]);
                            elemAud.setAttribute("language", langs[i]);
                        }
                    }
                } while (curProgram.moveToNext());
            }
            curProgram.close();
        }
    }
    
    private static void databaseToXml(final Context context, final SQLiteDatabase db, final String xmlPath) throws Exception {
        Log.d("TVDBTransformer", "Creating xml file " + xmlPath);
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        final Document document = builder.newDocument();
        document.setXmlVersion("1.0");
        final Element root = document.createElement("db");
        document.appendChild(root);
        final Cursor curSat = db.rawQuery("select * from sat_para_table", (String[])null);
        if (curSat != null) {
            if (curSat.moveToFirst()) {
                do {
                    final int dbSatID = getIntValue(curSat, "db_id", -1);
                    final Element elemSat = document.createElement("satellite");
                    root.appendChild(elemSat);
                    elemSat.setAttribute("name", getStringValue(curSat, "sat_name", ""));
                    elemSat.setAttribute("longitude", getStringValue(curSat, "sat_longitude", 0));
                    elemSat.setAttribute("lof_hi", getStringValue(curSat, "lof_hi", 10600));
                    elemSat.setAttribute("lof_lo", getStringValue(curSat, "lof_lo", 9750));
                    elemSat.setAttribute("lof_threshold", getStringValue(curSat, "lof_threshold", 11700));
                    elemSat.setAttribute("lnb_power", TVDBTransformer.lnbPowers[getIntValue(curSat, "voltage", 3)]);
                    elemSat.setAttribute("signal_22khz", TVDBTransformer.sig22K[getIntValue(curSat, "signal_22khz", 2)]);
                    elemSat.setAttribute("toneburst", TVDBTransformer.tonebursts[getIntValue(curSat, "tone_burst", 0)]);
                    elemSat.setAttribute("diseqc1_0", TVDBTransformer.diseqc10s[getIntValue(curSat, "committed_cmd", 4)]);
                    int diseqc11Val = getIntValue(curSat, "uncommitted_cmd", 4);
                    if (diseqc11Val < 240 || diseqc11Val > 255) {
                        diseqc11Val = 16;
                    }
                    else {
                        diseqc11Val -= 240;
                    }
                    elemSat.setAttribute("diseqc1_1", TVDBTransformer.diseqc11s[diseqc11Val]);
                    elemSat.setAttribute("motor", TVDBTransformer.motors[getIntValue(curSat, "diseqc_mode", 0)]);
                    final Cursor curTP = db.rawQuery("select * from ts_table where db_sat_para_id=" + dbSatID, (String[])null);
                    if (curTP != null && curTP.moveToFirst()) {
                        do {
                            final Element elemTP = document.createElement("transponder");
                            elemSat.appendChild(elemTP);
                            String networkID = "65535";
                            final Cursor curNet = db.rawQuery("select * from net_table where db_id=" + getIntValue(curTP, "db_net_id", -1), (String[])null);
                            if (curNet != null) {
                                if (curNet.moveToFirst()) {
                                    networkID = getStringValue(curNet, "network_id", 65535);
                                }
                                curNet.close();
                            }
                            elemTP.setAttribute("original_network_id", networkID);
                            elemTP.setAttribute("ts_id", getStringValue(curTP, "ts_id", 65535));
                            elemTP.setAttribute("frequency", getStringValue(curTP, "freq", 0));
                            elemTP.setAttribute("symbol_rate", getStringValue(curTP, "symb", 0));
                            elemTP.setAttribute("polarisation", (getIntValue(curTP, "polar", 1) == 0) ? "V" : "H");
                            createProgramElements(db, document, elemTP, getIntValue(curTP, "db_id", -1));
                        } while (curTP.moveToNext());
                    }
                } while (curSat.moveToNext());
            }
            curSat.close();
        }
        final Cursor curChan = db.rawQuery("select * from ts_table where db_sat_para_id < 0", (String[])null);
        if (curChan != null) {
            if (curChan.moveToFirst()) {
                do {
                    final Element elemChan = document.createElement("channel");
                    root.appendChild(elemChan);
                    final int src = getIntValue(curChan, "src", 0);
                    elemChan.setAttribute("fe_type", TVDBTransformer.feTypes[getIntValue(curChan, "src", 0)]);
                    elemChan.setAttribute("frequency", getStringValue(curChan, "freq", 0));
                    String networkID2 = "65535";
                    final Cursor curNet2 = db.rawQuery("select * from net_table where db_id=" + getIntValue(curChan, "db_net_id", -1), (String[])null);
                    if (curNet2 != null) {
                        if (curNet2.moveToFirst()) {
                            networkID2 = getStringValue(curNet2, "network_id", 65535);
                        }
                        curNet2.close();
                    }
                    elemChan.setAttribute("original_network_id", networkID2);
                    elemChan.setAttribute("ts_id", getStringValue(curChan, "ts_id", 65535));
                    if (src == 1) {
                        elemChan.setAttribute("symbol_rate", getStringValue(curChan, "symb", 0));
                        elemChan.setAttribute("modulation", TVDBTransformer.mods[getIntValue(curChan, "mod", 0)]);
                    }
                    else if (src == 2) {
                        elemChan.setAttribute("bandwidth", TVDBTransformer.bandwidths[getIntValue(curChan, "bw", 0)]);
                        elemChan.setAttribute("ofdm_mode", "dvbt");
                    }
                    else if (src == 3) {
                        elemChan.setAttribute("modulation", TVDBTransformer.mods[getIntValue(curChan, "mod", 0)]);
                    }
                    else if (src == 4) {
                        final int std = getIntValue(curChan, "std", 0);
                        final int vstd = TVChannelParams.VideoStd2Enum(std).toInt();
                        final int astd = TVChannelParams.AudioStd2Enum(std).toInt();
                        elemChan.setAttribute("video_standard", TVDBTransformer.atvVideoStds[vstd]);
                        elemChan.setAttribute("audio_standard", TVDBTransformer.atvAudioStds[astd]);
                        elemChan.setAttribute("sound_sys", "a2");
                    }
                    else if (src == 5) {
                        elemChan.setAttribute("bandwidth", TVDBTransformer.bandwidths[getIntValue(curChan, "bw", 0)]);
                    }
                    else if (src == 6) {
                        elemChan.setAttribute("bandwidth", TVDBTransformer.bandwidths[getIntValue(curChan, "bw", 0)]);
                    }
                    createProgramElements(db, document, elemChan, getIntValue(curChan, "db_id", -1));
                } while (curChan.moveToNext());
            }
            curChan.close();
        }
        createGroupElements(db, document, root);
        createChannelListElements(db, document, root);
        final TransformerFactory transFactory = TransformerFactory.newInstance();
        final Transformer transformer = transFactory.newTransformer();
        transformer.setOutputProperty("indent", "yes");
        transformer.setOutputProperty("method", "xml");
        transformer.setOutputProperty("encoding", "utf-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        final DOMSource domSource = new DOMSource(document);
        final File file = new File(xmlPath);
        final FileOutputStream fos = new FileOutputStream(file);
        final OutputStreamWriter osw = new OutputStreamWriter(fos);
        final Writer out = new BufferedWriter(osw);
        final StreamResult xmlResult = new StreamResult(out);
        transformer.transform(domSource, xmlResult);
    }
    
    private static int insertNet(final SQLiteDatabase db, final int fe_type, final int networkID) {
        final ContentValues cv = new ContentValues();
        int ret = -1;
        Cursor c = db.rawQuery("select * from net_table where src=" + fe_type + " and network_id=" + networkID, (String[])null);
        if (c != null) {
            if (c.moveToFirst()) {
                ret = getIntValue(c, "db_id", -1);
            }
            c.close();
        }
        if (ret < 0) {
            cv.clear();
            cv.put("src", Integer.valueOf(fe_type));
            cv.put("network_id", Integer.valueOf(networkID));
            cv.put("name", "");
            db.insert("net_table", "", cv);
            c = db.rawQuery("select * from net_table where src=" + fe_type + " and network_id=" + networkID, (String[])null);
            if (c != null) {
                if (c.moveToFirst()) {
                    ret = getIntValue(c, "db_id", -1);
                }
                c.close();
            }
        }
        return ret;
    }
    
    private static void insertSatellitePara(final Element elemSat, final SQLiteDatabase db) {
        final ContentValues cvSat = new ContentValues();
        cvSat.clear();
        cvSat.put("sat_name", getStringAttr(elemSat.getAttribute("name"), ""));
        cvSat.put("lnb_num", Integer.valueOf(0));
        cvSat.put("lof_hi", Integer.valueOf(getIntAttr(elemSat.getAttribute("lof_hi"), 9750)));
        cvSat.put("lof_lo", Integer.valueOf(getIntAttr(elemSat.getAttribute("lof_lo"), 10600)));
        cvSat.put("lof_threshold", Integer.valueOf(getIntAttr(elemSat.getAttribute("lof_threshold"), 11700)));
        cvSat.put("signal_22khz", Integer.valueOf(stringToValue(TVDBTransformer.sig22K, getStringAttr(elemSat.getAttribute("signal_22khz"), "auto"))));
        cvSat.put("voltage", Integer.valueOf(stringToValue(TVDBTransformer.lnbPowers, getStringAttr(elemSat.getAttribute("lnb_power"), "13/18v"))));
        cvSat.put("motor_num", Integer.valueOf(0));
        cvSat.put("pos_num", Integer.valueOf(0));
        cvSat.put("lo_direction", Integer.valueOf(0));
        cvSat.put("la_direction", Integer.valueOf(0));
        cvSat.put("longitude", Double.valueOf(0.0));
        cvSat.put("latitude", Double.valueOf(0.0));
        cvSat.put("sat_longitude", Integer.valueOf(getIntAttr(elemSat.getAttribute("longitude"), 0)));
        int diseqcMode = stringToValue(TVDBTransformer.motors, getStringAttr(elemSat.getAttribute("motor"), "none"));
        if (diseqcMode <= 2) {
            diseqcMode = 2;
        }
        cvSat.put("diseqc_mode", Integer.valueOf(diseqcMode));
        cvSat.put("tone_burst", Integer.valueOf(stringToValue(TVDBTransformer.tonebursts, getStringAttr(elemSat.getAttribute("touneburst"), "none"))));
        cvSat.put("committed_cmd", Integer.valueOf(stringToValue(TVDBTransformer.diseqc10s, getStringAttr(elemSat.getAttribute("diseqc1_0"), "none"))));
        int diseqc11Val = stringToValue(TVDBTransformer.diseqc11s, getStringAttr(elemSat.getAttribute("diseqc1_1"), "none"));
        if (diseqc11Val < 16) {
            diseqc11Val += 240;
        }
        else {
            diseqc11Val = 4;
        }
        cvSat.put("uncommitted_cmd", Integer.valueOf(diseqc11Val));
        cvSat.put("repeat_count", Integer.valueOf(0));
        cvSat.put("sequence_repeat", Integer.valueOf(0));
        cvSat.put("fast_diseqc", Integer.valueOf(0));
        cvSat.put("cmd_order", Integer.valueOf(0));
        db.insert("sat_para_table", "", cvSat);
    }
    
    private static void insertChannelList(final Element elemChanList, final SQLiteDatabase db) {
        final ContentValues cv = new ContentValues();
        final String strName = getStringAttr(elemChanList.getAttribute("name"), "");
        final int feType = stringToValue(TVDBTransformer.feTypes, getStringAttr(elemChanList.getAttribute("fe_type"), "dvbc"));
        final int mode = stringToValue(TVDBTransformer.feTypes, TVDBTransformer.strMode);
        if (mode != feType) {
            return;
        }
        Log.d("TVDBTransformer", "Inserting channel list " + strName);
        final NodeList entries = elemChanList.getElementsByTagName("channel_entry");
        if (entries != null && entries.getLength() > 0) {
            for (int j = 0; j < entries.getLength(); ++j) {
                final Element elemEntry = (Element)entries.item(j);
                Log.d("TVDBTransformer", "Inserting channel entry " + getIntAttr(elemEntry.getAttribute("frequency"), 0));
                cv.clear();
                cv.put("name", strName);
                cv.put("fe_type", Integer.valueOf(feType));
                cv.put("frequency", Integer.valueOf(getIntAttr(elemEntry.getAttribute("frequency"), 0)));
                cv.put("symbol_rate", Integer.valueOf(getIntAttr(elemEntry.getAttribute("symbol_rate"), 0)));
                cv.put("modulation", Integer.valueOf(stringToValue(TVDBTransformer.mods, getStringAttr(elemEntry.getAttribute("modulation"), "qpsk"))));
                cv.put("bandwidth", Integer.valueOf(stringToValue(TVDBTransformer.bandwidths, getStringAttr(elemEntry.getAttribute("bandwidth"), "8"))));
                cv.put("ofdm_mode", Integer.valueOf(stringToValue(TVDBTransformer.ofdmModes, getStringAttr(elemEntry.getAttribute("ofdm_mode"), "dvbt"))));
                db.insert("region_table", "", cv);
            }
        }
    }
    
    private static void insertGroup(final Document doc, final Element elemGroup, final SQLiteDatabase db) {
        final ContentValues cv = new ContentValues();
        final String strName = getStringAttr(elemGroup.getAttribute("name"), "");
        final String strProgramIds = getStringAttr(elemGroup.getAttribute("programs"), "");
        cv.clear();
        cv.put("name", strName);
        db.insert("grp_table", "", cv);
        final int dbGrpId = getNewInsertedRecordID(db, "grp_table");
        if (!strProgramIds.isEmpty() && dbGrpId >= 0) {
            final String[] strIds = strProgramIds.split(" ");
            if (strIds != null) {
                for (int i = 0; i < strIds.length; ++i) {
                    final Element elem = doc.getElementById(strIds[i]);
                    if (elem == null) {
                        Log.d("TVDBTransformer", "** Cannot find program id=" + strIds[i] + " in group " + strName + " **");
                    }
                    else {
                        cv.clear();
                        final int dbSrvId = getIntAttr(elem.getAttribute("db_id"), -1);
                        cv.put("db_srv_id", Integer.valueOf(dbSrvId));
                        cv.put("db_grp_id", Integer.valueOf(dbGrpId));
                        db.insert("grp_map_table", "", cv);
                    }
                }
            }
        }
    }
    
    private static void insertTP(final Element elemTP, final SQLiteDatabase db, final int dbNetID, final int dbSatID) {
        final ContentValues cv = new ContentValues();
        cv.clear();
        cv.put("src", Integer.valueOf(0));
        cv.put("db_net_id", Integer.valueOf(dbNetID));
        cv.put("ts_id", Integer.valueOf(65535));
        cv.put("freq", Integer.valueOf(getIntAttr(elemTP.getAttribute("frequency"), 0)));
        cv.put("symb", Integer.valueOf(getIntAttr(elemTP.getAttribute("symbol_rate"), 0)));
        cv.put("mod", Integer.valueOf(0));
        cv.put("bw", Integer.valueOf(0));
        cv.put("snr", Integer.valueOf(0));
        cv.put("ber", Integer.valueOf(0));
        cv.put("strength", Integer.valueOf(0));
        cv.put("db_sat_para_id", Integer.valueOf(dbSatID));
        cv.put("polar", Integer.valueOf((int)(getStringAttr(elemTP.getAttribute("polarisation"), "H").equals("H") ? 1 : 0)));
        cv.put("std", Integer.valueOf(0));
        cv.put("aud_mode", Integer.valueOf(0));
        cv.put("flags", Integer.valueOf(0));
        db.insert("ts_table", "", cv);
    }
    
    private static void insertTS(final Element elemTS, final SQLiteDatabase db, final int dbNetID) {
        final ContentValues cv = new ContentValues();
        final int fe_type = stringToValue(TVDBTransformer.feTypes, elemTS.getAttribute("fe_type"));
        cv.clear();
        cv.put("src", Integer.valueOf(fe_type));
        cv.put("db_net_id", Integer.valueOf(insertNet(db, fe_type, getIntAttr(elemTS.getAttribute("network_id"), 65535))));
        cv.put("ts_id", Integer.valueOf(getIntAttr(elemTS.getAttribute("ts_id"), 65535)));
        cv.put("freq", Integer.valueOf(getIntAttr(elemTS.getAttribute("frequency"), 0)));
        cv.put("symb", Integer.valueOf(getIntAttr(elemTS.getAttribute("symbol_rate"), 0)));
        cv.put("mod", Integer.valueOf(stringToValue(TVDBTransformer.mods, getStringAttr(elemTS.getAttribute("modulation"), "qpsk"))));
        cv.put("bw", Integer.valueOf(stringToValue(TVDBTransformer.bandwidths, getStringAttr(elemTS.getAttribute("bandwidth"), "8"))));
        cv.put("snr", Integer.valueOf(0));
        cv.put("ber", Integer.valueOf(0));
        cv.put("strength", Integer.valueOf(0));
        cv.put("db_sat_para_id", Integer.valueOf(-1));
        cv.put("polar", Integer.valueOf(-1));
        final int vstd = stringToValue(TVDBTransformer.atvVideoStds, getStringAttr(elemTS.getAttribute("video_standard"), "auto"));
        final int astd = stringToValue(TVDBTransformer.atvAudioStds, getStringAttr(elemTS.getAttribute("audio_standard"), "auto"));
        cv.put("std", Integer.valueOf(TVChannelParams.getTunerStd(vstd, astd)));
        cv.put("aud_mode", Integer.valueOf(0));
        cv.put("flags", Integer.valueOf(0));
        cv.put("dvbt_flag", Integer.valueOf(getIntAttr(elemTS.getAttribute("dvbt_flag"), 0)));
        db.insert("ts_table", "", cv);
    }
    
    private static void insertProgram(final Element elemProg, final SQLiteDatabase db, final int fe_type, final int dbNetID, final int dbTSID, final int dbSatID) {
        final ContentValues cv = new ContentValues();
        cv.clear();
        cv.put("src", Integer.valueOf(fe_type));
        cv.put("db_net_id", Integer.valueOf(dbNetID));
        cv.put("db_ts_id", Integer.valueOf(dbTSID));
        cv.put("name", getStringAttr(elemProg.getAttribute("name"), "No Name"));
        cv.put("service_id", Integer.valueOf(getIntAttr(elemProg.getAttribute("service_id"), 65535)));
        cv.put("service_type", Integer.valueOf(stringToValue(TVDBTransformer.srvTypes, getStringAttr(elemProg.getAttribute("type"), "other"))));
        cv.put("eit_schedule_flag", Integer.valueOf(0));
        cv.put("eit_pf_flag", Integer.valueOf(0));
        cv.put("running_status", Integer.valueOf(1));
        cv.put("free_ca_mode", Integer.valueOf(1));
        cv.put("volume", Integer.valueOf(50));
        cv.put("aud_track", Integer.valueOf(0));
        final NodeList video = elemProg.getElementsByTagName("video");
        if (video != null && video.getLength() > 0) {
            final Element vElem = (Element)video.item(0);
            cv.put("vid_pid", Integer.valueOf(getIntAttr(vElem.getAttribute("pid"), 8191)));
            cv.put("vid_fmt", Integer.valueOf(stringToValue(TVDBTransformer.vidFmts, getStringAttr(vElem.getAttribute("format"), "mpeg12"))));
        }
        else {
            cv.put("vid_pid", Integer.valueOf(8191));
            cv.put("vid_fmt", Integer.valueOf(-1));
        }
        cv.put("scrambled_flag", Integer.valueOf((int)(getStringAttr(elemProg.getAttribute("scrambled"), "false").equals("true") ? 1 : 0)));
        cv.put("current_aud", Integer.valueOf(-1));
        String apids = "";
        String afmts = "";
        String alangs = "";
        final NodeList audios = elemProg.getElementsByTagName("audio");
        if (audios != null && audios.getLength() > 0) {
            for (int i = 0; i < audios.getLength(); ++i) {
                final Element aElem = (Element)audios.item(i);
                if (i != 0) {
                    apids += " ";
                    afmts += " ";
                    alangs += " ";
                }
                apids += getStringAttr(aElem.getAttribute("pid"), "0x1fff");
                afmts += stringToValue(TVDBTransformer.audFmts, getStringAttr(aElem.getAttribute("format"), "mpeg"));
                alangs += getStringAttr(aElem.getAttribute("language"), "Audio" + (i + 1));
            }
        }
        cv.put("aud_pids", apids);
        cv.put("aud_fmts", afmts);
        cv.put("aud_langs", alangs);
        String spids = "";
        String stypes = "";
        String composition_page_ids = "";
        String ancillary_page_id = "";
        String slangs = "";
        final NodeList subtitles = elemProg.getElementsByTagName("subtitle");
        if (subtitles != null && subtitles.getLength() > 0) {
            for (int j = 0; j < subtitles.getLength(); ++j) {
                final Element sElem = (Element)subtitles.item(j);
                if (j != 0) {
                    spids += " ";
                    stypes += " ";
                    composition_page_ids += " ";
                    ancillary_page_id += " ";
                    slangs += " ";
                }
                spids += getStringAttr(sElem.getAttribute("pid"), "");
                stypes += getStringAttr(sElem.getAttribute("type"), "");
                composition_page_ids += getStringAttr(sElem.getAttribute("composition_page_id"), "");
                ancillary_page_id += getStringAttr(sElem.getAttribute("ancillary_page_id"), "");
                slangs += getStringAttr(sElem.getAttribute("language"), "");
            }
        }
        cv.put("current_sub", Integer.valueOf(-1));
        cv.put("sub_pids", spids);
        cv.put("sub_types", stypes);
        cv.put("sub_composition_page_ids", composition_page_ids);
        cv.put("sub_ancillary_page_ids", ancillary_page_id);
        cv.put("sub_langs", slangs);
        String tpids = "";
        String magazine_number = "";
        String page_number = "";
        String ttypes = "";
        String tlangs = "";
        final NodeList teletexts = elemProg.getElementsByTagName("teletext");
        if (teletexts != null && teletexts.getLength() > 0) {
            for (int k = 0; k < teletexts.getLength(); ++k) {
                final Element sElem2 = (Element)teletexts.item(k);
                if (k != 0) {
                    tpids += " ";
                    magazine_number += " ";
                    page_number += " ";
                    ttypes += " ";
                    tlangs += " ";
                }
                tpids += getStringAttr(sElem2.getAttribute("pid"), "");
                magazine_number += getStringAttr(sElem2.getAttribute("magazine_number"), "");
                page_number += getStringAttr(sElem2.getAttribute("page_number"), "");
                ttypes += getStringAttr(sElem2.getAttribute("type"), "");
                tlangs += getStringAttr(sElem2.getAttribute("language"), "");
            }
        }
        cv.put("current_ttx", Integer.valueOf(-1));
        cv.put("ttx_pids", tpids);
        cv.put("ttx_types", ttypes);
        cv.put("ttx_magazine_nos", magazine_number);
        cv.put("ttx_page_nos", page_number);
        cv.put("ttx_langs", tlangs);
        cv.put("chan_num", Integer.valueOf(getIntAttr(elemProg.getAttribute("channel_number"), 0)));
        cv.put("skip", Integer.valueOf((int)(getStringAttr(elemProg.getAttribute("skip"), "false").equals("true") ? 1 : 0)));
        cv.put("lock", Integer.valueOf((int)(getStringAttr(elemProg.getAttribute("parental_lock"), "false").equals("true") ? 1 : 0)));
        cv.put("favor", Integer.valueOf(0));
        cv.put("lcn", Integer.valueOf(0));
        cv.put("sd_lcn", Integer.valueOf(0));
        cv.put("hd_lcn", Integer.valueOf(0));
        cv.put("default_chan_num", Integer.valueOf(getIntAttr(elemProg.getAttribute("channel_number"), 0)));
        cv.put("chan_order", Integer.valueOf(getIntAttr(elemProg.getAttribute("channel_number"), 0)));
        cv.put("lcn_order", Integer.valueOf(getIntAttr(elemProg.getAttribute("channel_number"), 0)));
        cv.put("service_id_order", Integer.valueOf(getIntAttr(elemProg.getAttribute("channel_number"), 0)));
        cv.put("hd_sd_order", Integer.valueOf(getIntAttr(elemProg.getAttribute("channel_number"), 0)));
        cv.put("db_sat_para_id", Integer.valueOf(dbSatID));
        cv.put("dvbt2_plp_id", Integer.valueOf(getIntAttr(elemProg.getAttribute("dvbt2_plp_id"), 0)));
        cv.put("major_chan_num", Integer.valueOf(getIntAttr(elemProg.getAttribute("major_chan_num"), 0)));
        cv.put("minor_chan_num", Integer.valueOf(getIntAttr(elemProg.getAttribute("minor_chan_num"), 0)));
        cv.put("access_controlled", Integer.valueOf(0));
        cv.put("hidden", Integer.valueOf(0));
        cv.put("hide_guide", Integer.valueOf(0));
        cv.put("source_id", Integer.valueOf(0));
        cv.put("encrypt", Integer.valueOf((int)(getStringAttr(elemProg.getAttribute("encrypt"), "false").equals("true") ? 1 : 0)));
        db.insert("srv_table", "", cv);
        elemProg.setAttribute("db_id", Integer.toString(getNewInsertedRecordID(db, "srv_table")));
    }
    
    private static int getNewInsertedRecordID(final SQLiteDatabase db, final String table) {
        int id = -1;
        final Cursor c = db.rawQuery("select * from " + table + " order by db_id desc limit 1", (String[])null);
        if (c != null) {
            if (c.moveToFirst()) {
                id = getIntValue(c, "db_id", -1);
            }
            c.close();
        }
        return id;
    }
    
    private static void xmlToDatabase(final Context context, final SQLiteDatabase db, final String xmlPath) throws Exception {
        Log.d("TVDBTransformer", "Clearing database ...");
        db.execSQL("delete from net_table");
        db.execSQL("delete from ts_table");
        db.execSQL("delete from srv_table");
        db.execSQL("delete from evt_table");
        db.execSQL("delete from booking_table");
        db.execSQL("delete from grp_table");
        db.execSQL("delete from grp_map_table");
        db.execSQL("delete from dimension_table");
        db.execSQL("delete from sat_para_table");
        db.execSQL("delete from region_table");
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        factory.setValidating(true);
        factory.setNamespaceAware(true);
        Log.d("TVDBTransformer", "Set entity resolver");
        builder.setEntityResolver(new MyEntityResolver());
        final ErrorHandler h = new MyErrorHandler();
        builder.setErrorHandler(h);
        Document dom;
        try {
            if (xmlPath.equals("tv_default.xml")) {
                final AssetManager assetManager = context.getAssets();
                final InputStream is = assetManager.open(xmlPath);
                dom = builder.parse(is);
            }
            else {
                final InputStream is_new = new FileInputStream(xmlPath);
                dom = builder.parse(is_new);
            }
        }
        catch (Exception e) {
            Log.d("TVDBTransformer", e.toString());
            throw new InvalidFileException();
        }
        final Element root = dom.getDocumentElement();
        final NodeList satellites = root.getElementsByTagName("satellite");
        if (satellites != null && satellites.getLength() > 0) {
            for (int i = 0; i < satellites.getLength(); ++i) {
                final Element elemSat = (Element)satellites.item(i);
                insertSatellitePara(elemSat, db);
                final int dbSatID = getNewInsertedRecordID(db, "sat_para_table");
                final NodeList tps = elemSat.getElementsByTagName("transponder");
                if (tps != null && tps.getLength() > 0) {
                    for (int j = 0; j < tps.getLength(); ++j) {
                        final Element elemTP = (Element)tps.item(j);
                        final int dbNetID = insertNet(db, 0, getIntAttr(elemTP.getAttribute("original_network_id"), 65535));
                        insertTP(elemTP, db, dbNetID, dbSatID);
                        final int dbTSID = getNewInsertedRecordID(db, "ts_table");
                        final NodeList progs = elemTP.getElementsByTagName("program");
                        if (progs != null && progs.getLength() > 0) {
                            for (int k = 0; k < progs.getLength(); ++k) {
                                insertProgram((Element)progs.item(k), db, 0, dbNetID, dbTSID, dbSatID);
                            }
                        }
                    }
                }
            }
        }
        final NodeList chans = root.getElementsByTagName("channel");
        if (chans != null && chans.getLength() > 0) {
            Log.d("TVDBTransformer", "Insertint channels");
            for (int l = 0; l < chans.getLength(); ++l) {
                final Element elemTS = (Element)chans.item(l);
                final String strFe = getStringAttr(elemTS.getAttribute("fe_type"), "dvbt");
                final int dbNetID2 = insertNet(db, stringToValue(TVDBTransformer.feTypes, strFe), getIntAttr(elemTS.getAttribute("original_network_id"), 65535));
                Log.d("TVDBTransformer", "Inserting ts");
                insertTS(elemTS, db, dbNetID2);
                final int dbTSID2 = getNewInsertedRecordID(db, "ts_table");
                final NodeList progs2 = elemTS.getElementsByTagName("program");
                if (progs2 != null && progs2.getLength() > 0) {
                    for (int m = 0; m < progs2.getLength(); ++m) {
                        Log.d("TVDBTransformer", "Inserting program");
                        insertProgram((Element)progs2.item(m), db, stringToValue(TVDBTransformer.feTypes, strFe), dbNetID2, dbTSID2, -1);
                    }
                }
            }
        }
        final NodeList chanLists = root.getElementsByTagName("channel_list");
        for (int i2 = 0; i2 < chanLists.getLength(); ++i2) {
            insertChannelList((Element)chanLists.item(i2), db);
        }
        final NodeList groups = root.getElementsByTagName("program_group");
        for (int i3 = 0; i3 < groups.getLength(); ++i3) {
            insertGroup(dom, (Element)groups.item(i3), db);
        }
    }
    
    public static void transform(final Context context, final int transDirection, final SQLiteDatabase db, final String xmlPath, final String mode) throws Exception {
        if (transDirection == 1) {
            TVDBTransformer.strMode = mode;
            xmlToDatabase(context, db, xmlPath);
        }
        else if (transDirection == 0) {
            databaseToXml(context, db, xmlPath);
        }
    }
    
    static {
        feTypes = new String[] { "dvbs", "dvbc", "dvbt", "atsc", "analog", "dtmb", "isdbt" };
        srvTypes = new String[] { "unknown", "tv", "radio", "atv", "data", "dtv", "playback", "other" };
        vidFmts = new String[] { "mpeg12", "mpeg4", "h264", "mjpeg", "real", "jpeg", "vc1", "avs" };
        audFmts = new String[] { "mpeg", "pcm_s16le", "aac", "ac3", "alaw", "mulaw", "dts", "pcm_s16be", "flac", "cook", "pcm_u8", "adpcm", "amr", "raac", "wma", "wma_pro", "pcm_bluray", "alac", "vorbis", "aac_latm", "ape", "eac3", "pcm_wifidisplay" };
        mods = new String[] { "qpsk", "qam16", "qam32", "qam64", "qam128", "qam256", "qamauto", "vsb8", "vsb16", "psk8", "apsk16", "apsk32", "dqpsk" };
        bandwidths = new String[] { "8", "7", "6", "auto", "5", "10", "1_712" };
        lnbPowers = new String[] { "13v", "18V", "off", "13/18v" };
        sig22K = new String[] { "on", "off", "auto" };
        tonebursts = new String[] { "none", "bursta", "burstb" };
        diseqc10s = new String[] { "lnb1", "lnb2", "lnb3", "lnb4", "none" };
        diseqc11s = new String[] { "lnb1", "lnb2", "lnb3", "lnb4", "lnb5", "lnb6", "lnb7", "lnb8", "lnb9", "lnb10", "lnb11", "lnb12", "lnb13", "lnb14", "lnb15", "lnb16", "none" };
        motors = new String[] { "none", "none", "none", "diseqc1.2", "diseqc1.3" };
        ofdmModes = new String[] { "dvbt", "dvbt2" };
        atvVideoStds = new String[] { "auto", "pal", "ntsc", "secam" };
        atvAudioStds = new String[] { "dk", "i", "bg", "m", "l", "auto" };
        TVDBTransformer.strMode = "dvbc";
    }
    
    public static class InvalidFileException extends Exception
    {
    }
    
    private static class MyErrorHandler implements ErrorHandler
    {
        public void warning(final SAXParseException e) throws SAXException {
            Log.d("TVDBTransformer", "Warning: ");
            this.printInfo(e);
        }
        
        public void error(final SAXParseException e) throws SAXException {
            Log.d("TVDBTransformer", "Error: ");
            this.printInfo(e);
        }
        
        public void fatalError(final SAXParseException e) throws SAXException {
            Log.d("TVDBTransformer", "Fattal error: ");
            this.printInfo(e);
        }
        
        private void printInfo(final SAXParseException e) {
            Log.d("TVDBTransformer", "   Public ID: " + e.getPublicId());
            Log.d("TVDBTransformer", "   System ID: " + e.getSystemId());
            Log.d("TVDBTransformer", "   Line number: " + e.getLineNumber());
            Log.d("TVDBTransformer", "   Column number: " + e.getColumnNumber());
            Log.d("TVDBTransformer", "   Message: " + e.getMessage());
        }
    }
    
    public static class MyEntityResolver implements EntityResolver
    {
        public InputSource resolveEntity(final String publicId, final String systemId) throws SAXException, IOException {
            Log.d("TVDBTransformer", "resolveEntity, systemId is " + systemId);
            if (systemId.equals("tv_default.dtd")) {
                return new InputSource("tv_default.dtd");
            }
            return null;
        }
    }
}
