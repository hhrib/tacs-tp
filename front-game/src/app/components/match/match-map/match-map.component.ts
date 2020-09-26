import { Component, OnInit } from '@angular/core';


declare var ol: any;

@Component({
  selector: 'match-map',
  templateUrl: './match-map.component.html',
  styleUrls: ['./match-map.component.css']
})
export class MatchMapComponent implements OnInit {
  latitude: number = -60.5;
  longitude: number = -37;

  map: any;

  ngOnInit() {
    this.map = new ol.Map({
      target: 'map',
      controls: ol.control.defaults({
        attributionOptions: {
          collapsible: false
        }
      }),
      layers: [
        new ol.layer.Tile({
          source: new ol.source.OSM()
        }),
      ],
      view: new ol.View({
        center: ol.proj.fromLonLat([-60.5, -37]),
        zoom: 6,
        minZoom: 5,
        maxZoom: 7,
      })
    });

    this.map.on('click', function (args) {
      console.log(args.coordinate);
      var lonlat = ol.proj.transform(args.coordinate, 'EPSG:3857', 'EPSG:4326');
      console.log(lonlat);
      
      var lon = lonlat[0];
      var lat = lonlat[1];
      //alert(`lat: ${lat} long: ${lon}`);
    });
  }
}