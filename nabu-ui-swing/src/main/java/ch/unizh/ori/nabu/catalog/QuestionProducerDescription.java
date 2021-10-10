package ch.unizh.ori.nabu.catalog;

import ch.unizh.ori.nabu.core.QuestionProducer;
import java.util.List;

public interface QuestionProducerDescription {

	String getName();

	String getKey();

	String getDescription();

	QuestionProducer createProducer();

	List<QuestionProducerDescription> getSubQuestionProducerDescriptions();

}