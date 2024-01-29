const fs = require('fs');
let buf = '';

process.stdin.on('readable', function() {
  const chunk = process.stdin.read();
  if (chunk) buf += chunk.toString();
});

process.stdin.on('end', function() {
  buf.split('\n').forEach(function(line) {
    const tokens = line.split(' ').map(function (x) {
      return parseInt(x);
    });
    if (tokens.length !== 2) return;
    console.log(tokens.reduce(function(a, b) { return a + b; }));
  });
});
