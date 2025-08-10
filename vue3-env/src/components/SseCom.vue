<template>
  <div id="events"></div>
</template>

<script setup>
const eventSource = new EventSource("http://localhost:5000/sse/stream");

eventSource.onmessage = (event) => {
  document.getElementById("events").innerHTML += `<p>${event.data}</p>`;
};

eventSource.onerror = (error) => {
  console.error("SSE Error:", error);
  eventSource.close();
};
</script>
