var socketio = require('socket.io');
var io;
var guestNumber  =1;
var nickNames = {};
var namesUsed = [];
var currentRoom = {};
//启动Socket.IO服务器
exports.listen = function(server){
    io = socketio.listen(server);
    io.set('log level', 1);
    io.sockets.on('connection',function(socket){   //定义每个用户连接的处理逻辑
        guestNumber  =assignGuestName(socket,guestNumber,nickNames,namesUsed);//在用户连接上来时
        //赋予其一个访客名
        joinRoom(socket,'Lobby');
        handleMessageBroadcasting(socket,nickNames);//处理用户的消息，更名以及聊天时的创建和变更
        handleNameChangeAttempts(socket,nickNames,namesUsed);
        handleRoomJoining(socket);
        socket.on('room',function(){    //用户发出请求时向其提供已被占用的聊天室的列表
            socket.emit('rooms',io.sockets.manager.rooms);
        });
        handleClientDisconnection(socket,nickNames,namesUsed);//定义用户断开连接后的清除逻辑
    });
};
//分配用户昵称
function assignGuestName(socket,guestNumber,nickNames,nameUsed){
                 var name = 'Guest' + guestNumber;//生成新昵称
                 nickNames[socket.id]=name;
                 socket.emit('nameResult',{//让用户知道他们的昵称
                     success: true,
                     name: name
                 });
                 nameUsed.push(name);//存放已经被占用的昵称
                 return guestNumber + 1;
}
//进入聊天室
function joinRoom(socket,room){
    socket.join(room);
    currentRoom[socket.id] = room;
    socket.emit('joinResult',{room: room});
    socket.broadcast.to(room).emit('message',{
        text: nickNames[socket.id] + 'has joined ' +room+'.'
    });
    var usersInRoom = io.sockets.clients(room);
    if(usersInRoom.length>1){
        var usersInRoomSummary  ='Users currently in ' + room +':';
        for(var index in  usersInRoom) {
            if (usersInRoom.hasOwnProperty(index)) {
                var userSocketId = usersInRoom[index].id;
                if (userSocketId !== socket.id) {
                    if (index > 0) {
                        usersInRoomSummary += ', ';
                    }
                    usersInRoomSummary += nickNames[userSocketId];
                }
            }
        }
        usersInRoomSummary+='.';
        socket.emit('message', {text:usersInRoomSummary});
    }
}
//处理昵称变更请求
function handleNameChangeAttempts(socket,nickNames,namesUsed){
    socket.on('nameAttempt',function(name){
        if(name.indexOf('Guest')===0){
            socket.emit('nameResult',{
                success:false,
                message: 'Names cannot begin with "Guest".'
            });
        }
        else{
            if(namesUsed.indexOf(name)===-1){
                var previousName = nickNames[socket.id];
                var previousNameIndex = namesUsed.indexOf(previousName);
                namesUsed.push(name);
                nickNames[socket.id] = name;
                delete namesUsed[previousNameIndex];
                socket.emit('nameResult',{
                    success:true,
                    name:name
                });
                socket.broadcast.to(currentRoom[socket.id]).emit('message',{
                    text:previousName + 'is now known as ' + name + '.'
                });
            }
            else{
                socket.emit('nameResult',{
                    success:false,
                    message:'That name is already in use.'
                });
            }
        }
    });
}
//发送聊天消息
// 转发消息
function handleMessageBroadcasting(socket){
    socket.on('message',function(message){
        socket.broadcast.to(message.room).emit('message',{
            text: nickNames[socket.id] + ': ' +message.text
        });
    });
}
//创建房间
function handleRoomJoining(socket){
    socket.on('join',function(room){
        socket.leave(currentRoom[socket.id]);
        joinRoom(socket, room.newRoom);
    });
}
//用户断开连接
function handleClientDisconnection(socket){
    socket.on('disconnect',function(){
        var nameIndex = namesUsed.indexOf(nickNames[socket.id]);
        delete namesUsed[nameIndex];
        delete nickNames[socket.id];
    });
}
