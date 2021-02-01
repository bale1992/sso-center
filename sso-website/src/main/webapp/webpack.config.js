const path = require('path');
const {CleanWebpackPlugin} = require("clean-webpack-plugin");
const HtmlWebpackPlugin = require("html-webpack-plugin");

module.exports = {
    entry: {
        loginPage: './source/login/index.js',
        modifyPasswdPage: './source/modifypasswd/index.js',
        addUserPage: './source/adduser/index.js'
    },
    output: {
        path: path.join(__dirname, './dist'),
        filename: '[name].bundle.js'
    },
    resolve: {
        extensions: ['.wasm', '.mjs', '.js', '.json', '.jsx']
    },
    module: {
        rules: [
            {
                test: /\.(js|jsx)$/,
                exclude: /node_modules/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        babelrc: false,
                        presets: [
                            require.resolve('@babel/preset-react'),
                            [require.resolve('@babel/preset-env'), {modules: false}]
                        ],
                        cacheDirectory: true
                    }
                }
            }
        ]
    },
    plugins: [
        new HtmlWebpackPlugin({
            template: path.join(__dirname, '/source/index.html'),
            filename: 'AddUserPage.html',
            title: '创建用户',
            chunks: ['addUserPage']
        }),
        new HtmlWebpackPlugin({
            template: path.join(__dirname, '/source/index.html'),
            filename: 'LoginPage.html',
            title: '登陆',
            chunks: ['loginPage']
        }),
        new HtmlWebpackPlugin({
            template: path.join(__dirname, '/source/index.html'),
            filename: 'ModifyPasswdPage.html',
            title: '修改初始密码',
            chunks: ['modifyPasswdPage']
        }),
        new CleanWebpackPlugin()
    ],
    mode: 'development'
};
