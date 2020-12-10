package aug.bueno.c1.l5.orm.is.norm.repository;

import aug.bueno.c1.l5.orm.is.norm.entity.Delivery;
import org.apache.ibatis.annotations.*;

import java.util.Date;

@Mapper
public interface DeliveryMapper {

    @Select("SELECT * FROM Delivery WHERE id = #{deliveryId}")
    Delivery findDelivery(int deliveryId);

    @Insert("INSERT INTO Delivery (orderId, time) " +
            "VALUES(#{orderId}, #{time})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertDelivery(Delivery delivery);

    @Delete("DELETE FROM Delivery WHERE id = #{deliveryId}")
    void deleteDelivery(int deliveryId);
}
