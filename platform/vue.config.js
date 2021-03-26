const webpack = require("webpack");


module.exports = {
    publicPath: '/',
    outputDir: 'dist', // 打包的目录
    lintOnSave: true, // 在保存时校验格式
    productionSourceMap: false, // 生产环境是否生成 SourceMap
    devServer: {
        open: true, // 启动服务后是否打开浏览器
        host: 'localhost',
        disableHostCheck: true,
        port: 8111, // 服务端口
        https: false,
        hotOnly: false,
        proxy: {
            '/xk': { //名字必须跟application context相同 否则404
                target: 'http://localhost:8686',
                changeOrigin: true, //  跨域
                pathRewrite: {
                    '^/xk': '/xk'
                }
            }
        }, // 设置代理
        before: app => {}
    },

    configureWebpack: {
        //支持jquery
        plugins: [
            new webpack.ProvidePlugin({
                $:"jquery",
                jQuery:"jquery",
                "windows.jQuery":"jquery"
            })
        ]
    },
}