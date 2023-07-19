let http=require("http");
let serve=http.createServer(function(req,res){
    res.writeHead(200,{'Content-type':'text/html'});
    res.end('<marquee>Hello world</marquee>');
});
serve.listen(3000);