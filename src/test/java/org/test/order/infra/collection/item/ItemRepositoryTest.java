//package org.test.order.infra.collection.item;
//
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.test.order.infra.repository.ItemMongoRepository;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataMongoTest
//@ExtendWith(SpringExtension.class)
//@AutoConfigureDataJpa
//@RequiredArgsConstructor
//public class ItemRepositoryTest {
//
//    private final ItemMongoRepository itemMongoRepository;
//
//    @Test
//    public void testCreatedAtAndUpdatedAt() {
//        UUID uuid = UUID.randomUUID();
//        Item item = new Item(uuid, "Test Item", 10.0, 2);
//        itemMongoRepository.save(item);
//
//        Item savedItem = itemMongoRepository.findById(uuid).orElse(null);
//        assertThat(savedItem).isNotNull();
//        assertThat(savedItem.getCreatedAt()).isNotNull();
//        assertThat(savedItem.getUpdatedAt()).isNotNull();
//        assertThat(savedItem.getCreatedAt()).isEqualTo(savedItem.getUpdatedAt());
//
//        savedItem.setName("Updated Test Item");
//        itemMongoRepository.save(savedItem);
//
//        Item updatedItem = itemMongoRepository.findById(uuid).orElse(null);
//        assertThat(updatedItem).isNotNull();
//        assertThat(updatedItem.getUpdatedAt()).isAfter(updatedItem.getCreatedAt());
//    }
//}