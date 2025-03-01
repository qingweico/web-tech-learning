import {afterAll, beforeAll, test} from '@jest/globals';

import {getRedisClient, closeRedisClient} from "@/lib/utils/RedisClient";
import Redis from "ioredis";

let redis: Redis
beforeAll(() => {
    redis = getRedisClient()
})

test('redis set get string', async () => {
    redis.set("key", "value")
    const result = await redis.get("key")
    console.log(result)
})
afterAll(() => {
    closeRedisClient()
})
