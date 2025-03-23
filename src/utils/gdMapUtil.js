import AMapLoader from "@amap/amap-jsapi-loader";

export default {
  // 通用的AMap插件加载方法
  initAMap() {
    return AMapLoader.load({
      key: "8f42ed925abc432d7dd1bc221997e8d2",
      version: "2.0",
      plugins: [
        "AMap.CitySearch",
        "AMap.Geolocation",
        "AMap.AutoComplete",
        "AMap.PlaceSearch",
        "AMap.Geocoder"
      ]
    }).catch(error => {
      console.error('AMap SDK 加载失败:', error);
      return Promise.reject(error);
    });
  },

  // 获取城市信息
  getCityData(plugin, callback) {
    return new Promise((resolve, reject) => {
      this.initAMap().then(AMap => {
        AMap.plugin(plugin, () => {
          callback(resolve, reject, AMap);
        });
      }).catch(reject);
    });
  },

  // 获取当前城市
  getCity() {
    return this.getCityData('AMap.CitySearch', (resolve, reject, AMap) => {
      const citySearch = new AMap.CitySearch({
        enableHighAccuracy: true,
        timeout: 10000,
        offset: [10, 20],
        zoomToAccuracy: true,
        position: 'RB'
      });
      citySearch.getLocalCity((status, result) => {
        if (status === 'complete') {
          resolve(result);
        } else {
          reject(new Error('get city error'));
        }
      });
    });
  },

  // 获取定位的城市信息
  getLocationCity() {
    return this.getCityData('AMap.Geolocation', (resolve, reject, AMap) => {
      const geolocation = new AMap.Geolocation({
        enableHighAccuracy: true,
        timeout: 10000,
        offset: [10, 20],
        zoomToAccuracy: true,
        position: 'RB'
      });
      geolocation.getCityInfo((status, result) => {
        if (status === 'complete') {
          resolve(result);
        } else {
          reject(new Error('get location error'));
        }
      });
    });
  },

  // 获取自动补全搜索结果
  getSearchOptions(query) {
    return new Promise((resolve, reject) => {
      this.initAMap().then(AMap => {
        const autocomplete = new AMap.AutoComplete();
        autocomplete.search(query, (status, result) => {
          if (status === 'complete') {
            resolve(result);
          } else {
            reject(new Error('get search error'));
          }
        });
      }).catch(reject);
    });
  },

  // 设置地图标记
  setMarker(lng, lat) {
    return new Promise((resolve, reject) => {
      this.initAMap().then(AMap => {
        const marker = new AMap.Marker({
          position: new AMap.LngLat(lng, lat), // 经纬度对象
        });
        resolve(marker);
      }).catch(reject);
    });
  },

  // 进行地点搜索
  getPlaceSearch(keyword) {
    return new Promise((resolve, reject) => {
      this.initAMap().then(AMap => {
        const placeSearch = new AMap.PlaceSearch();
        placeSearch.search(keyword, (status, result) => {
          if (status === 'complete') {
            resolve(result);
          } else {
            reject(new Error('get place search error'));
          }
        });
      }).catch(reject);
    });
  },

  // 获取地点详情
  getPlaceSearchDetail(poiid) {
    return new Promise((resolve, reject) => {
      this.initAMap().then(AMap => {
        const placeSearch = new AMap.PlaceSearch();
        placeSearch.getDetails(poiid, (status, result) => {
          if (status === 'complete') {
            resolve(result);
          } else {
            reject(new Error('get place search error'));
          }
        });
      }).catch(reject);
    });
  },

  // 逆地理编码获取地址
  GeocoderGetAddress(lng, lat) {
    return new Promise((resolve, reject) => {
      this.initAMap().then(AMap => {
        const geocoder = new AMap.Geocoder();
        geocoder.getAddress([lng, lat], (status, result) => {
          if (status === 'complete' && result.info === 'OK') {
            resolve(result);
          } else {
            reject(new Error('get geocoder address error'));
          }
        });
      }).catch(reject);
    });
  },

  // 销毁地图实例
  destroyMap(mapInstance) {
    mapInstance?.destroy();
  },
}
