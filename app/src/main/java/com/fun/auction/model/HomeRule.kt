package com.`fun`.auction.model

data class HomeRule(
    val content: Content = Content(),
    val video_url: String = "" // http://pfile.qupaipaia.com/qupaipai/video/1622630359320279.mp4
)

data class Content(
    val content: String = "" // ① 拍拍宝盒分为三个场次。每个场次起拍价格不同，可拍中商品也会不同；② 每个场次下，可单拍可十连拍，百分百拍出商品，十连拍必中精品；③ 拍中宝盒可实物收货，系统会自动发出物流单，也可委托平台转卖，为了保证转卖成功，转卖价格可能会低于商品市场价，转卖成功后系统会将转卖金额存入个人账户余额，可用于提现和拍拍币充值；④ 每个场次下，拍满10个宝盒可免费领取1个进阶宝盒，每天一共可领取5个进阶宝盒（三个场次一共15个），进阶宝盒可开出的商品价值也会随等级进阶；⑤ 每个场次下的超级宝盒，代表本场次可拍中价值最高的商品和优选精品，超级宝盒会不定期更新，真实随机开拍，拍中必发；
)