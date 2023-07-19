function save(item){
    let playListArray = getStoreArray("SongList");
    playListArray.push(item);
    localStorage.setItem("SongList",JSON.stringify(playListArray));
}
function loadPlayList(){
    let list = getSavePlayList();
    let ul = document.getElementById("SongList");
    if(list !=null){
        for(let i = 0;i < list.length;i++){
            let li = document.createElement("li");
            li.innerHTML = list[i];
            ul.appendChild(li);
        }
    }               
}
function getSavePlayList(){
    return getStoreArray("SongList");
}
function getStoreArray(key){
    let playListArray = localStorage.getItem(key);
    if(playListArray == null || playListArray === ""){
        playListArray = [];
    }
    else{
        playListArray = JSON.parse(playListArray); 
    }
    return playListArray;
}