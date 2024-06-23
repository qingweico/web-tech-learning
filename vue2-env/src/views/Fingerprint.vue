<template>
  <div>
    <button @click="getFpId">获取浏览器指纹ID</button>
    <div>
      visitorId: {{ visitorId }}
    </div>
  </div>
</template>

<script>
import FingerprintJS from '@fingerprintjs/fingerprintjs'
export default {
  name: 'Fingerprint',
  data() {
    return {
      visitorId: null
    }
  },
  methods: {
    async getFpId() {
      // Initialize an agent at application startup.
      const fpPromise = FingerprintJS.load()
      ;await (async () => {
        // Get the visitor identifier when you need it.
        const fp = await fpPromise
        const result = await fp.get()
        this.visitorId = result.visitorId
      })()
    },
  },
}
</script>
