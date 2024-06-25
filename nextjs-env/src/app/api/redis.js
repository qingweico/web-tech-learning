import Redis from 'ioredis';

const redis = new Redis(process.env.REDIS_URL); // 使用环境变量配置 Redis 连接字符串

export default async function handler(req, res) {
    if (req.method === 'GET') {
        // 从 Redis 中获取数据
        const value = await redis.get('my-key');
        res.status(200).json({ value });
    } else if (req.method === 'POST') {
        // 向 Redis 中设置数据
        const { key, value } = req.body;
        await redis.set(key, value);
        res.status(200).json({ message: 'Key set successfully' });
    } else {
        res.status(405).json({ message: 'Method not allowed' });
    }
}
