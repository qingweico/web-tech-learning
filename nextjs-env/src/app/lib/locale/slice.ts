import {getJsonData, JSONObject} from '@/lib/json';
import {apiSlice} from '@/app/lib/api/slice';
import {LocaleLeafObject} from '@/app/lib/locale/common';

export const extendedApiSlice = apiSlice.injectEndpoints({
    endpoints: (builder) => ({
        getLocaleData: builder.query<JSONObject, { path: string; locale: string }>({
            query: ({path, locale}) => ({
                url: '/locales',
                params: {path, locale},
            }),
            transformResponse: (rawResult: JSONObject) => rawResult.data as JSONObject,
        }),
    }),
});

const {useGetLocaleDataQuery} = extendedApiSlice;

/**
 * @param dataPath The path to the locale data, including the path to the file, and the JSON path within the file.
 *                 e.g. "/path/to/file#path.to.data".
 * @param locale A single "language tag" in the format defined in [RFC 5646](https://datatracker.ietf.org/doc/html/rfc5646).
 */
export function useLocaleData(dataPath: string, locale: string) {
    const [filePath, jsonPath] = dataPath.split('#');

    return useGetLocaleDataQuery({path: filePath, locale}, {
        selectFromResult: (result) => {
            if (!result.data) return result;
            return {...result, data: getJsonData(result.data, jsonPath) as LocaleLeafObject};
        },
    });
}
