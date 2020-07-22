$(document).ready(function(){
    $("#uid").text("loading");

    if( window.Android ) {
        $("#uid").text(window.Android.getUID());
    } else {
        $("#uid").text("api not loaded");
    }
  });