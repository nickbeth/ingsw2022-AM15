package it.polimi.ingsw.eriantys.network;

import it.polimi.ingsw.eriantys.model.RuleBook;
import it.polimi.ingsw.eriantys.model.enums.GameMode;

import java.io.Serializable;
import java.util.List;

public class GameInfo implements Serializable {
  private GameMode mode;
  private List<String> playersNickname;

  public RuleBook getRules() {
    return RuleBook.makeRules(mode, playersNickname.size());
  }

  public GameMode getMode() {
    return mode;
  }

  public List<String> getPlayersNickname() {
    return playersNickname;
  }
}
