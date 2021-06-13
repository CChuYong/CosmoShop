package chuyong.cosmoshop.internal;

import lombok.Getter;

public enum MessageConstants {
    PAGE_NOT_EXISTS("그 페이지는 존재하지 않습니다."),
    ERROR_CANT_BUY("구매할 수 없는 상품입니다."),
    ERROR_CANT_SELL("판매할 수 없는 상품입니다."),
    ERROR_NOT_ENOUGH_PAYMENT("%s가 부족합니다."),
    ERROR_NOT_ENOUGH_SPACE("인벤토리 공간이 부족합니다."),
    PURCHSE_SUCCESS("%s를 이용한 구매에 성공했습니다. 남은 잔액: %d %s."),
    SELL_SUCCESS("%s를 이용한 판매에 성공했습니다. 남은 잔액: %d %s."),
    ERROR_CANT_SELL_NOT_ENOUGH_ITEM("판매하고자 하는 아이템이 부족합니다."),
    UNKNOWN_SHOP("알 수 없는 상점 명입니다.")
    ;
    private final static String prefix = "§f[ §6상점 §f] ";
    @Getter
    private String message;
    MessageConstants(String message){
        this.message = message;
    }
    public String getMessageWithPrefix(){
        return new StringBuilder(prefix).append(message).toString();
    }
}
