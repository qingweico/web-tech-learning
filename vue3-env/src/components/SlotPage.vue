<template>
  <h3>{{ symbol }}</h3>
  <div>
    <UdOperation>
      <template v-for="slotName in slots" #[slotName]/>
    </UdOperation>
  </div>
  <h3>{{ '作用域插槽' }}</h3>
  <SlotScope v-slot="slotProps">
    {{ slotProps.text }} {{ slotProps.count }}
  </SlotScope>

  <h3>{{ '具名作用域插槽' }}</h3>
  <NamedSlotScope>
    <!--v-slot:NamedSlotScope="props"-->
    <template #NamedSlotScope="props">
      {{ props }}
    </template>
  </NamedSlotScope>
  <h3>{{ '同时使用了具名插槽与默认插槽' }}</h3>
  <BothSlotPage>
    <template  #default="{ message }">
      <p>{{ message }}</p>
    </template>
    <template #footer>
      <!-- message 属于默认插槽,此处不可用 -->
      <p>{{ message }}</p>
    </template>
  </BothSlotPage>
</template>
<script setup>

import UdOperation from "@/components/UdOperation.vue";
import {ref} from "vue";
import SlotScope from "@/components/SlotScope.vue";
import NamedSlotScope from "@/components/NamedSlotScope.vue";
import BothSlotPage from "@/components/BothSlotPage.vue";

const slots = ref(['default', 'edit', 'del', 'setting']);
const symbol = Symbol('SlotPage').toString()
</script>
