/**
 * 开发环境
 */
;(function () {
  window.SITE_CONFIG = {};

  // api接口请求地址,在renren-fast-vue首页点击登录时，访问到http://localhost:5050/api，由于路由1请求转发到http://localhost:8080/renren-fast/
  window.SITE_CONFIG['baseUrl'] = 'http://localhost:5050/api';

  // cdn地址 = 域名 + 版本号
  window.SITE_CONFIG['domain']  = './'; // 域名
  window.SITE_CONFIG['version'] = '';   // 版本号(年月日时分)
  window.SITE_CONFIG['cdnUrl']  = window.SITE_CONFIG.domain + window.SITE_CONFIG.version;
})();
