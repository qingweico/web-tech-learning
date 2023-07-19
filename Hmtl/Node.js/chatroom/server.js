let http=require('http');//内置的http模板提供了http服务器和服务端功能
let fs=require("fs");//内置的path模板提供了与文件系统路径相关的功能
let path=require("path");//内置的http模板提供了http服务器和服务端功能
let mime=require("mime");//附加的mime功能有根据文件扩展文得出MIME类型的功能
let cache={};//cache是用来缓存文件内容的对象
//请求文件不存在时发送404错误
function send404(response){
    response.writeHead(404,{'Content-Type':'text/plain'});
    response.write('Error 404:resource not found.');
    response.end();
}
//提供文件数据服务
function sendFile(response,filePath,fileContents){
    response.writeHead(
        200,
        {'content-type':mime.lookup(path.basename(filePath))}
    );
    response.end(fileContents);
}
//提供静态文件服务
function serveStatic(response,cache,absPath){
    if(cache[absPath]){                                //检查文件是否缓存在内存中
        sendFile(response,absPath,cache[absPath]);     //从文件中返回文件
    }else{
        fs.exists(absPath,function(exists){            //检查文件请是否存在
           if(exists){
               fs.readFile(absPath,function(err,data){  //从硬盘中读取文件
                   if(err){
                       send404(response);
                   }else{
                       cache[absPath]=data;
                       sendFile(response,absPath,data); //从硬盘中读取文件并返回
                   }
               });
           }
           else{
               send404(response);                 //发送HTTP 404响应
           }
        });
    }
}
//创建HTTP服务器的逻辑
let server= http.createServer (function(request,response){
    let filePath= false;
    if(request.url==='/'){
        filePath = 'public/index.html';
    }
    else{
        filePath = 'public' + request.url;
    }
    let absPath='./' + filePath;
    serveStatic(response,cache,absPath);
});
//启动服务器
server.listen(3000,function(){
    console.log("Server listening on port 3000.");
});
//设置Socket.IO服务器
let chatServer = require('./lib/chat_server');
chatServer.listen(server);