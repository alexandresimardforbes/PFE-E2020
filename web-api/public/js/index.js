$(document).ready(function(){
    $("#uid").text("loading");

    if( window.Android ) {
        $("#uid").text(window.Android.getUID());
    } else {
        $("#uid").text("api not loaded");
    }
  });
  
$.getScript('https://luke-chang.github.io/js-spatial-navigation/spatial_navigation.js', function() {
  $('a, .focusable')
    .SpatialNavigation()
    .focus()
    .blur()
    .first()
    .focus();

    window.addEventListener('sn:enter-up', function(evt) {
      $('#'+evt.target.id).triggerHandler("click");
    });
    
});

function toggleVisibility() {
  console.log("buttonClick");
  $('.background').toggleClass("invisible");
}
