<template>
  <div ref="editor"></div>
</template>

<script>

import Editor from "wangeditor";

export default {
  name: "MyEditor",
  props: {
    placeholder: {
      type: String,
    },
    modelValue: {
      type: String,
      default: "",
    },
    disabled: {
      type: Boolean,
      default: false,
    },
    menus: {
      type: Array,
      default: () => null,
    },
  },
  emits: ["update:modelValue"],
  data() {
    return {
      defaultMenus: ["head", "bold", "fontSize", "fontName", "italic", "underline", "strikeThrough", "indent", "lineHeight", "foreColor", "backColor", "link", "list", "justify", "quote", "emoticon", "image", "table", "code", "splitLine", "undo", "redo"],
      editor: null,
      innerValue: "",
    };
  },
  watch: {
    modelValue: {
      handler(newValue) {
        if (this.editor && newValue !== this.innerValue) {
          this.innerValue = newValue;
          this.editor.txt.html(newValue);
        }
      },
      immediate: true,
    },
    placeholder(newV, oldV) {
      if (newV !== oldV && this.$refs.editor) {
        try {
          this.$refs.editor.querySelector(".placeholder").innerHTML = newV;
        } catch (e) {
          console.warn("更新占位符失败:", e);
        }
      }
    },
    disabled(newV) {
      if (!this.editor) return;

      if (newV === true) {
        this.editor.disable();
      } else {
        this.editor.enable();
      }
    },
  },
  mounted() {
    this.$nextTick(() => {
      this.initEditor();
    });
  },
  methods: {
    initEditor() {
      this.editor = new Editor(this.$refs.editor);
      this.editor.config.uploadImgShowBase64 = false;
      this.editor.config.uploadImgServer = "";
      this.editor.config.uploadFileName = "file";
      this.editor.config.uploadImgHeaders = {"token": ""};
      this.editor.config.placeholder = this.placeholder || "请输入";
      this.editor.config.uploadImgHooks = {};
      this.editor.config.uploadImgMaxSize = 20 * 1024 * 1024;
      this.editor.config.showLinkImg = false;
      this.editor.config.zIndex = 100;
      this.setMenus();
      this.editor.config.onchange = (html) => {
        this.innerValue = html;
        this.$emit("update:modelValue", html);
      };
      this.editor.create();
      if (this.disabled === true) {
        this.editor.disable();
      } else {
        this.editor.enable();
      }
      if (this.modelValue) {
        this.innerValue = this.modelValue;
        this.setHtml(this.modelValue);
      }
    },
    setMenus() {
      this.editor.config.menus = this.menus || this.defaultMenus;
    },
    getHtml() {
      return this.editor && this.editor.txt ? this.editor.txt.html() : "";
    },
    setHtml(txt) {
      if (this.editor && this.editor.txt) {
        this.editor.txt.html(txt);
      }
    },
  },
  beforeUnmount() {
    if (this.editor) {
      this.editor.destroy();
      this.editor = null;
    }
  },
};
</script>
