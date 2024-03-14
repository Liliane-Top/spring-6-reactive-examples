package nl.top.spring6reactiveexamples.repositories;

import nl.top.spring6reactiveexamples.domain.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PersonRepositoryImpl implements PersonRepository {

    Person liane = Person.builder().id(1).firstName("Liane").lastName("Top").build();
    Person yo = Person.builder().id(2).firstName("Yo").lastName("van Gaselt").build();
    Person ton = Person.builder().id(3).firstName("Ton").lastName("Kraak").build();
    Person robert = Person.builder().id(4).firstName("Robert").lastName("van Leeuwen").build();

    @Override
    public Mono<Person> getById(Integer id) {
        return Mono.just(liane);
    }

    @Override
    public Flux<Person> findAll() {

        return Flux.just(liane, yo, ton, robert);
    }
}
