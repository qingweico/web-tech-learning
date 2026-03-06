package cn.qingweico.common.util.encryption;


import lombok.Data;

/**
 * @author zhouqingwei
 */
@Data
public class EncryptedString {
    /**
     * AES偏移量 长度为16个字符
     */
    public static String key = "29wBT78mIfQDwER6";
    /**
     * 长度为16个字符
     */
    public static String iv = "EBBQADSwAwSAJBAK";
    /**
     * 私钥
     */
    public static String priKey = """
            MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAp3vICfcyxS5hjcTPx8CuV1STflSQ
            qhlTEygmR7C1hGzp8ToWWBTE29wBT78mIfQDwER6fyDiRqp4Dw/jMAksLQIDAQABAkAYrnsqTM2k
            lVM3Czv7mf+XSxV5VokDKDw/5HjPs9DsNMrXwUNcApybJE50l9OKkOr1LJA8CprSREiyDarqBQNB
            AiEA9QBZIUSSBuscrDG5fpwr3DzX/5N0p9UuaaawV/7S1IkCIQCvAJFa7vxI11R4B25aAdH5cU5U
            11i1uoHcub0WCqx5hQIhANPSxzziIjblE9c07qGuuKUj3cRUYumwgP5LZe/Om82xAiB5GDZ8quRN
            7xqXF+VHSP0n0zy1vnG3BoC34Jqz9tzUFQIgEV2JUCKLPU5njjNfBoNmz/choQ7nGFM2MBFyDWl7
            0Ik=
            """.strip().replaceAll("\\s+", "");
    /**
     * 公钥
     */
    public static String pubKey = """
            MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKd7yAn3MsUuYY3Ez8fArldUk35UkKoZUxMoJkewtYRs
            6fE6FlgUxNvcAU+/JiH0A8BEen8g4kaqeA8P4zAJLC0CAwEAAQ==
            """.strip().replaceAll("\\s+", "");
}
