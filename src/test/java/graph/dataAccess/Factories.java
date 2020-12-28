package graph.dataAccess;

import java.util.HashSet;
import java.util.Optional;
import java.util.Random;

import graph.models.Artist;
import graph.models.Label;

public class Factories {
  public static Artist ArtistFactory() {
    Long discogsId = new Random().nextLong();
    return new Artist(
        discogsId,
        "Artist" + discogsId,
        "Good",
        Optional.empty(),
        new HashSet<>(),
        Optional.empty(),
        new HashSet<>(),
        Optional.empty()
    );
  }
  public static Label LabelFactory() {
    Long discogsId = new Random().nextLong();
    return new Label(
        discogsId,
        "OK",
        Optional.empty(),
        Optional.empty()
    );
  }
}
