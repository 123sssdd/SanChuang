<template>
    <div id="guide-map" style="width: 100%;height: 100%;">
      <div style="display: flex;justify-content: center;">
        <el-select v-model="targetAddress" placeholder="搜索地址" style="width: 60%;z-index: 1;margin-top: 10px"
                   size="small"
                   filterable
                   remote
                   value-key="id"
                   :remote-method="getSearch"
                   :loading="loading"
                   @change="currentSelect">
          <el-option
              v-for="item in addressOptions"
              :key="item.id"
              :label="item.name"
              :value="item"
          >
            <span style="float: left;">{{ item.name}}</span>
            <span style="float: right; color: #8492a6; font-size: 13px;margin-left: 10px">{{ item.address }}</span>
          </el-option>
        </el-select>
      </div>
    </div>
</template>

<script>
import gdMapUtil from "@/utils/gdMapUtil";

export default {
  mounted() {
     gdMapUtil.getLocationCity().then(res=>{
       this.mapCenter = [res.position[0], res.position[1]]
       //绘制地图
       this.getMap();
       //绘制标记
       this.setMark(res.position[0], res.position[1])
     },error=>{
       //绘制地图
       this.getMap();
     })
  },
  data() {
    return {
      loading: true,
      mapCenter:[],
      map: null,
      marker:null,
      targetAddress: null,
      addressOptions: [],
    }
  },
  methods: {
    getMap() {
      gdMapUtil.initAMap().then(AMap => {
        this.map = new AMap.Map("guide-map", {
          viewMode: '2D', //默认使用 2D 模式
          zoom: 11, //地图级别
          center: this.mapCenter, //地图中心点
        });
      })
    },
    getSearch(query) {
      this.addressOptions=[]
      if(query ==='') return
      gdMapUtil.getPlaceSearch(query).then(res=>{
        this.loading = false;
        this.addressOptions = res.poiList.pois
      })
    },
    setMark(lng,lat){
      if(this.marker){
        this.map.remove(this.marker)
      }
      gdMapUtil.setMarker(lng, lat).then(marker=>{
        this.marker = marker
        this.map.add(marker)
      })
    },
    currentSelect(val) {
      if (!val) return;
      this.targetAddress = val
      this.map.setFitView();
      //设置marker
      this.setMark(val.location.lng, val.location.lat)
      //更新地图中心点
      this.map.setCenter([val.location.lng, val.location.lat])
      this.$emit("close-drawer", val)
    },
  },
  destroyed() {
    gdMapUtil.destroyMap();
  }
}
</script>

<style scoped lang="less">

#guide-map {
  width: 100%;
  height: 100%;
}


</style>