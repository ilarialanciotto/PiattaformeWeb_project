package org.ilaria.progetto.Service;

import lombok.RequiredArgsConstructor;
import org.ilaria.progetto.Model.DTO.FeedbackDTO;
import org.ilaria.progetto.Repository.FeedbackRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public void save(FeedbackDTO feedback) {
        feedbackRepository.save(feedback.getFeedback());
    }

}
