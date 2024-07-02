import 'server-only';
import assert from 'assert';
import fsp from 'fs/promises';
import path from 'path';
import {getJsonData, LocaleLeafObject} from '@/app/lib/locale/common';

export * from '@/app/lib/locale/common';

/**
 * @param dataPath The path to the locale data, including the path to the file, and the JSON path within the file.
 *                 e.g. "/path/to/file#path.to.data".
 * @param locale A single "language tag" in the format defined in [RFC 5646](https://datatracker.ietf.org/doc/html/rfc5646).
 */
export async function getLocaleData(dataPath: string, locale: string): Promise<LocaleLeafObject> {
    assert(locale);

    const [filePath, jsonPath] = dataPath.split('#');

    const dataDir = path.dirname(filePath);
    const baseName = path.basename(filePath);
    const fullPath = path.join(process.cwd(), 'public', 'locales', locale, dataDir, `${baseName}.json`);

    const fileData = await fsp.readFile(fullPath, 'utf-8');
    let jsonData = JSON.parse(fileData);
    return getJsonData(jsonData, jsonPath) as LocaleLeafObject;
}

export function calcInitialLocale(headers: Headers) {
    const acceptLanguage = headers.get('Accept-Language');
    if (acceptLanguage === null) return 'en';
    return /zh(-|,|;|$)/.test(acceptLanguage) ? 'zh-CN' : 'en';
}
