<template>
  <div>
    <slot :message="'hello'"></slot>
    <slot name="footer" />
  </div>

</template>
