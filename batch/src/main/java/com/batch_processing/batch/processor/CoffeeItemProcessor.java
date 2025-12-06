package com.batch_processing.batch.processor;

import com.batch_processing.batch.entity.Coffee;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemProcessor;

@Slf4j
public class CoffeeItemProcessor implements ItemProcessor<Coffee, Coffee> {


    @Override
    public @Nullable Coffee process(final Coffee item) throws Exception {

        Long id = item.getId();
        String brand = item.getBrand().toUpperCase();
        String origin = item.getOrigin().toUpperCase();
        String characterstics = item.getCharacterstics().toUpperCase();

        Coffee transformedCoffee = new Coffee(id, brand, origin, characterstics);

        log.info("Converting ({}) into ({}) ", item , transformedCoffee);

        return transformedCoffee;
    }
}
