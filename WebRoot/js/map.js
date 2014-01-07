(function($) {

  var g_map,
      g_mark,
      g_geocoder,
      div_map_id = "map",
      $btn_search,
      $btn_submit,
      $i_address;
  
  
  $(function() {
    map_init(location.search.substring(1).split(','));
    
    $i_address = $("#address");
    
    $btn_search = $("#search").click(function() {
      var val = $i_address.val();
      var $this = $(this);
      if (val) {
        g_geocoder.geocode({ 'address': val }, function(results, status) {
          if (status === 'OK') {
            g_map.setCenter(results[0].geometry.location);
            g_mark.setPosition(results[0].geometry.location);
          } else {
            alert("地址无法解析。");
          }
        });
      } else {
        alert("请输入查询地址。");
        $i_address.focus();
      }
    });
    
    $btn_submit = $("#submit").click(function() {
      var position = g_mark.getPosition();
      if (position && opener) {
        var position_val = position.lat() + "," + position.lng();
        opener.$("#googleLocalSpan").html(position_val);
        opener.$("#googleLocal").val(position_val);
        window.close();
      } else {
        alert("您还没有定位");
        $i_address.focus();
      }
    });
  });

  function map_init(path) {
    if (path.length < 2) { path[0] = 29.819464; path[1] = 121.5504072 } 
    g_geocoder = new google.maps.Geocoder();
    var latLng = new google.maps.LatLng(path[0], path[1]);
    var map_options = {
      zoom: 14, 
      center: latLng, 
      mapTypeId: google.maps.MapTypeId.ROADMAP, 
      disableDefaultUI: true,
      zoomControl: true,
      zoomControlOptions: {
        style: google.maps.ZoomControlStyle.LARGE,
        position: google.maps.ControlPosition.LEFT_CENTER
      }
    }
    g_map = new google.maps.Map(document.getElementById(div_map_id), map_options);
    g_mark = new google.maps.Marker({ map: g_map, draggable: true, position: latLng });
    
    google.maps.event.addListener(g_map, 'zoom_changed', function() {
      g_map.setCenter(g_mark.getPosition());
    });
  }

})(jQuery);
