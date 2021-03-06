/*
 * Copyright 2013- Yan Bonnel
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.ybonnel.simpleweb4j.samples.computers;

import fr.ybonnel.simpleweb4j.model.SimpleEntityManager;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Computer {

    @Id
    @GeneratedValue
    public Long id;
    public String name;
    @Temporal(TemporalType.DATE)
    public Date introduced;
    @Temporal(TemporalType.DATE)
    public Date discontinued;
    @ManyToOne(fetch = FetchType.EAGER)
    public Company company;


    public static SimpleEntityManager<Computer, Long> simpleEntityManager = new SimpleEntityManager<>(Computer.class);
}
