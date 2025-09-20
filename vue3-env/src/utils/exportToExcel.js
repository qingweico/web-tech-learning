// 一个 JavaScript 的 Excel 处理库, 可以在 浏览器 或 Node.js 里读取、编辑、生成 Excel 文件
import * as XLSX from "xlsx";

/**
 * 从表格元素导出Excel
 * @param {string} id - 表格元素的ID
 * @param {string} defaultTitle - 默认的文件标题
 * @param {string} sheetName - 工作表名称
 */
export function export_table_to_excel(id, defaultTitle, sheetName = "SheetJS") {
    const table = document.getElementById(id);
    if (!table) return;

    // 获取表格数据
    const wb = XLSX.utils.table_to_book(table, {sheet: sheetName});

    // 设置列宽
    const ws = wb.Sheets[sheetName];
    const cols = [];
    for (let i = 0; i < 12; i++) {
        cols.push({wch: 20}); // 设置列宽为20个字符
    }
    ws["!cols"] = cols;

    // 导出文件
    const title = defaultTitle || "列表";
    XLSX.writeFile(wb, `${title}.xlsx`);
}

/**
 * 从JSON数据导出Excel
 * @param {Array} th - 表头数组
 * @param {Array} jsonData - JSON数据数组
 * @param {string} defaultTitle - 默认的文件标题
 * @param {string} sheetName - 工作表名称
 */
export function export_json_to_excel(th, jsonData, defaultTitle, sheetName = "SheetJS") {
    // 组合数据
    const data = [th, ...jsonData];

    // 创建工作簿和工作表
    const wb = XLSX.utils.book_new();
    const ws = XLSX.utils.aoa_to_sheet(data);

    // 设置列宽
    const cols = [];
    for (let i = 0; i < th.length; i++) {
        cols.push({wch: 20}); // 设置列宽为20个字符
    }
    ws["!cols"] = cols;

    // 添加工作表到工作簿
    XLSX.utils.book_append_sheet(wb, ws, sheetName);

    // 导出文件
    const title = defaultTitle || "列表";
    XLSX.writeFile(wb, `${title}.xlsx`);
}
