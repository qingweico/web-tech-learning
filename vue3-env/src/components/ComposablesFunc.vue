<script setup>
import { useMouse } from './mouse.js'
const { x, y } = useMouse()
</script>

<template>Mouse position is at: {{ x }}, {{ y }}</template>
