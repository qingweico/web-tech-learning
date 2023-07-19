window.onload = function(){
    loadPlayList();
    document.getElementById("buttonClick").onclick = handleButtonClick;
    function handleButtonClick(){
      let text = document.getElementsByTagName("input")[0];
      let songName = text.value;
      if(songName === ''){
          alert("歌曲名不能为空!");
      }else{
          let li = document.createElement("li");
          li.innerHTML = songName;
          let ul = document.getElementById("SongList");
          ul.appendChild(li);
          document.getElementsByTagName("input")[0].value='';
          save(songName);
    }
} 
}