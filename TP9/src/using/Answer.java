package using;

import java.util.Comparator;
import java.util.Objects;

public record Answer(String site, String item, Integer price) implements Comparable<Answer> {
  public Answer {
    Objects.requireNonNull(site);
    Objects.requireNonNull(item);
    if (price <= 0) {
      throw new IllegalArgumentException();
    }
  }

  /**
   * The Comparator used to compare Answers
   */
  public static Comparator<Answer> ANSWER_COMPARATOR = Comparator
      .comparing(Answer::price, Comparator.nullsLast(Comparator.naturalOrder()))
      .thenComparing(Answer::site, Comparator.naturalOrder())
      .thenComparing(Answer::item, Comparator.naturalOrder());

  @Override
  public String toString() {
    return item + "@" + site + " : " + price;
  }

  @Override
  public int compareTo(Answer o) {
    return ANSWER_COMPARATOR.compare(this, o);
  }
}