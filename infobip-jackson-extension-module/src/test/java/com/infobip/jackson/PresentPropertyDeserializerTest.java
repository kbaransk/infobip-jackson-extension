package com.infobip.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

@AllArgsConstructor
class PresentPropertyDeserializerTest extends TestBase {

    @Test
    void shouldDeserializeRoadBikeAsBikeFromJson() throws JsonProcessingException {
        // given
        String json = "{'roadBike':'roadBike'}";

        // when
        Bike actual = objectMapper.readValue(json, Bike.class);

        // then
        then(actual).isEqualTo(new RoadBike("roadBike"));
    }

    @Test
    void shouldDeserializeRoadBikeAsBikeFromSerializedRoadBike() throws JsonProcessingException {
        // given
        String json = objectMapper.writeValueAsString(new RoadBike("roadBike"));

        // when
        Bike actual = objectMapper.readValue(json, Bike.class);

        // then
        then(actual).isEqualTo(new RoadBike("roadBike"));
    }

    @Test
    void shouldDeserializeBmxAsBikeFromJson() throws JsonProcessingException {
        // given
        String json = "{'bmx':'bmx'}";

        // when
        Bike actual = objectMapper.readValue(json, Bike.class);

        // then
        then(actual).isEqualTo(new Bmx("bmx"));
    }

    @Test
    void shouldDeserializeBmxAsBikeFromSerializedBmx() throws JsonProcessingException {
        // given
        String json = objectMapper.writeValueAsString(new Bmx("bmx"));

        // when
        Bike actual = objectMapper.readValue(json, Bike.class);

        // then
        then(actual).isEqualTo(new Bmx("bmx"));
    }

    @Test
    void shouldDeserializeListOfBikes() throws JsonProcessingException {
        // given
        String json = objectMapper.writeValueAsString(Arrays.asList(new RoadBike("roadBike"), new Bmx("bmx")));

        // when
        List<Bike> actual = objectMapper.readValue(json, new TypeReference<List<Bike>>() {
        });

        // then
        then(actual).isEqualTo(Arrays.asList(new RoadBike("roadBike"), new Bmx("bmx")));
    }

    @Test
    void shouldDeserializeBikeWithMultipleMatchingProperties() throws JsonProcessingException {
        // given
        String json = "{'bmx':'', 'roadBike': 'roadBike'}";

        // when
        Bike actual = objectMapper.readValue(json, Bike.class);

        // then
        then(actual).isEqualTo(new RoadBike("roadBike"));
    }

    @Test
    void shouldDeserializeRoadBikeAsRoadBikeFromJson() throws JsonProcessingException {
        // given
        String json = "{'roadBike':'roadBike'}";

        // when
        RoadBike actual = objectMapper.readValue(json, RoadBike.class);

        // then
        then(actual).isEqualTo(new RoadBike("roadBike"));
    }

    interface Bike extends PresentPropertyJsonHierarchy<BikeType> {

    }

    @AllArgsConstructor(onConstructor_ = @JsonCreator)
    @Value
    static class RoadBike implements Bike {

        private final String roadBike;
    }

    @AllArgsConstructor(onConstructor_ = @JsonCreator)
    @Value
    static class Bmx implements Bike {

        private final String bmx;
    }

    @Getter
    @AllArgsConstructor
    enum BikeType implements TypeProvider {
        ROAD_BIKE(RoadBike.class),
        BMX(Bmx.class);

        private final Class<? extends Bike> type;
    }
}
