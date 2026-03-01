package codenames.service;

import codenames.model.CardType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GameService {
    public List<CardType> assignCardTypes() {
        List<CardType> cardTypes = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            cardTypes.add(CardType.RED);
        }

        for (int i = 0; i < 8; i++) {
            cardTypes.add(CardType.BLUE);
        }

        for (int i = 0; i < 7; i++) {
            cardTypes.add(CardType.NEUTRAL);
        }

        cardTypes.add(CardType.ASSASSIN);

        Collections.shuffle(cardTypes);

        return cardTypes;
    }
}
