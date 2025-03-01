import Redis from 'ioredis';
const redisOptions = {
    host: process.env.REDIS_HOST || 'localhost',
    port: Number(process.env.REDIS_PORT) || 6379,
    db: Number(process.env.REDIS_DB) || 0,
    lazyConnect: true,
    connectionPool: {
        max: 100,
        min: 20,
        idleTimeoutMillis: 10000
    }
};
let redis = new Redis(redisOptions);

export const getRedisClient = () => {
    return redis.duplicate()
}
export const closeRedisClient = () => {
    /**
     * direct shutdown
     *
     * {@link Redis#quit} shutdown gracefully
     */
    redis.disconnect()
}
