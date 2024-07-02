export type JSONPrimitive = string | number | boolean | null;
export type JSONValue = JSONPrimitive | JSONObject | JSONArray;
export type JSONObject = { [member: string]: JSONValue };

export interface JSONArray extends Array<JSONValue> {
}

/**
 * Drill down to the json object
 */
export function getJsonData(jsonObject: JSONObject, path: string | undefined): JSONValue {
    if (!path) return jsonObject;

    const pathElements = path.split('.');
    let currData = jsonObject;
    for (const pathElement of pathElements) currData = currData[pathElement] as JSONObject;
    return currData;
}
