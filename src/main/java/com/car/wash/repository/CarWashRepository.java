/**
 * 
 */
package com.car.wash.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.car.wash.model.CarWashModel;

/**
 * @author ANGSHUMAN
 *
 */
@Repository
public interface CarWashRepository extends MongoRepository<CarWashModel, String>{

}
