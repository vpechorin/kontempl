<#macro swiper timer_speed=10000 pause_on_start=true width="656" height="437">
<#assign imgAltSuffix =  cmslib.getPageElementContent(txt, "sitename")>
<#if pagenodeImages??>
<#if (pagenodeImages?size < 2)>
<#list pagenodeImages as img>
<img src="${appConfig.fileStorageUrl}${img.getAbsolutePath()}" width=${width} height=${height} alt="${txt.title} - ${img.name} - ${imgAltSuffix}">
</#list>
<script>
$(document).ready(function() {
$(".swipebox").swipebox({hideBarsDelay : 0});
});
</script>    
<#else>
<div class="swiper-container">
    <div class="swiper-wrapper">
    <#list pagenodeImages as img>
        <div class="swiper-slide">
            <img src="${appConfig.fileStorageUrl}${img.getAbsolutePath()}" width=656 height=437 alt="${txt.title} - ${img.name} - ${imgAltSuffix}">
        </div>            
    </#list>
    </div>
    <a class="swiper-prev" href="#" id=swiperNextBtn>Prev<span></span></a>
    <a class="swiper-next" href="#" id=swiperPrevBtn>Next<span></span></a>
</div>
<div class="pagination"></div>

<script>
$(function(){
  var mySwiper = $('.swiper-container').swiper({
    mode:'horizontal',
    loop: true,
    autoplay: ${timer_speed},
    keyboardControl: true,
    mousewheelControl: true,
    pagination: '.pagination',
    paginationClickable: true,
    grabCursor: true,
    preventLinks: true,
    simulateTouch: true
  });
  $('.swiper-prev').on('click', function(e){
    e.preventDefault();
    mySwiper.stopAutoplay();
    mySwiper.swipePrev();
  });
  $('.swiper-next').on('click', function(e){
    e.preventDefault();
    mySwiper.stopAutoplay();
    mySwiper.swipeNext()
  });
  <#if pause_on_start>
  mySwiper.stopAutoplay();
  </#if>
})

</script>    
</#if>
</#if>
</#macro>