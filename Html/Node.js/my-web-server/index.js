

/*let fs=require("fs");
fs.readdir(__dirname,function(err,files){
    console.log(files);
})*/


//------------------------------------------------------------------
//获取当前目录下所有的文件
                 /*同步版本的readdir*/
//console.log(require("fs").readdirSync(__dirname));
                 /*异步版本的readdir
function async(err,files){  console.log(files)}
require("fs").readdir('.',async);*/


//-----------------------Node应用 获取当前目录下所有的文件的内容----------------------
  let fs=require('fs');
  stdout=process.stdout;
  stdin=process.stdin;
const stats = [];
fs.readdir(process.cwd(),function(err,files){
      console.log('');
      if(!files.length){
          console.log('  \033[31m  No files to show\033[39m\n');
      }
      console.log("Select which file or directory you want to see\n");
      function file(i){
          const filename = files[i];
          fs.stat(__dirname +"/"+ filename,function(err,stat){
            stats[i]=stat;
            if(stat.isDirectory()){
                console.log('     '+i+'   \033[36m' + filename+'/\033[39m');
            }
            else{
                console.log('     '+i+'   \033[90m' + filename +'/\033[39m');
            }
            if(++i === files.length){
                read();
            }else{
                file(i);
            }
          });
      }
      file(0);
      function read(){
        console.log('');
        stdout.write('    \033[33mEnter your choice : \033[39m');
        stdin.resume();
        stdin.setEncoding("UTF-8");
        stdin.on('data',option);
      }
      function option(data){
          const filename = files[Number(data)];
          if(!filename){
            stdout.write('    \033[33mEnter your choice : \033[39m');
          }
          if(stats[Number(data)].isDirectory()){
            fs.readdir(__dirname + '/' +filename , function(err,files){
                console.log('');
                console.log('     ('+ files.length + 'files)');
                files.forEach(function(file){
                    console.log('    -    '+file);
                });
                console.log('');
            });
          }
          else {
              stdin.pause();
              fs.readFile(__dirname + '/'+filename,'utf-8', function(err,data){
                  console.log('');
                  console.log('\033[90m' + data.replace(/(.*)/g, '    $1')+'\033[39m');
              });
          }
      }
  })
