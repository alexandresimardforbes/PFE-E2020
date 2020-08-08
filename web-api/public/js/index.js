
  window.InputMenuVisible = true;
  resetActivity();


$(document).ready(function () {
  $("#uid").text("loading");

  if (window.Android) {
  }

  $.getScript(
    "https://luke-chang.github.io/js-spatial-navigation/spatial_navigation.js",
    function () {
      $("a, .focusable").SpatialNavigation().focus().blur().first().focus();

      window.addEventListener("sn:enter-up", function (evt) {
        $("#" + evt.target.id).triggerHandler("click");
      });
    }
  );

  if (window.Android) {
    window.addEventListener("keyup", function onKeyUp(event) {
      if (event.isComposing || event.keyCode === 34) {
        window.Android.channelDown();
        setTimeout(updateChannel, 2000);
      }
      if (event.isComposing || event.keyCode === 33) {
        window.Android.channelUp();
        setTimeout(updateChannel, 2000);
      }
    });

    updateChannel();
    $("#uid").text(window.Android.getUID());
  } else {
    $("#uid").text("api not loaded");
  }
});

function updateChannel() {
  if (!window.Android) {
    return;
  }

  $("#channel-number").text("Channel " + window.Android.getChannel());
}

function startAnalogTelevision() {
  window.InputMenuVisible = false;
  hideMenus();
  $(".background").fadeOut();
}

function openInputMenu() {
  window.InputMenuVisible = true;
  $("#header").fadeIn();
}

function open() {
  console.log("buttonClick");
  $(".background").toggleClass("invisible");
}

function simulateKeyPress(code) {
  $.event.trigger({ type: "keypress", which: code });
}

window.addEventListener("keyup", function onKeyUp(event) {
  if (window.InputMenuVisible) {
    $("#header").fadeIn();
  }
  $("#channel").fadeIn();
  resetActivity();
});

function hideMenus() {
  $("#header").fadeOut();
  $("#channel").fadeOut();
}

function hideMenus() {
  $("#header").fadeOut();
  $("#channel").fadeOut();
}

function resetActivity() {
  clearTimeout(window.resetActivityTimer);
  window.resetActivityTimer = setTimeout(hideMenus, 5000);
}
