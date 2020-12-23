package graph.models;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class Label extends BaseNode {

  @Relationship(type="HasSubLabel") private Set<Label> subLabels;
  @Relationship(type="HasParentLabel") private Label parentLabel;

  public Optional<Set<Label>> getSubLabels() {
    return this.subLabels.isEmpty() ? Optional.empty() : Optional.of(this.subLabels);
  }
  public Optional<Label> getParentLabel() {
    return this.parentLabel == null ? Optional.empty() : Optional.of(this.parentLabel);
  }

  private void addSublabel(Label subLabel) {
    this.subLabels.add(subLabel);
  }
  private void setParentLabel(Label parent) {
    this.parentLabel = parent;
  }

  /**
   * Establish the HasSubLabel and HasParentLabel relationships between
   * a parent label and a sublabel.
   *
   * @param parentLabel
   * @param subLabel
   */
  public static void createParentSubLabelRelationship(Label parentLabel, Label subLabel) {
    parentLabel.addSublabel(subLabel);
    subLabel.setParentLabel(parentLabel);
  }

  public static Class<Label> getEntityType() {
    return Label.class;
  }

  public Label(
      Long discogsId,
      String dataQuality,
      Optional<Set<Label>> subLabels,
      Optional<Label> parentLabel
      ) {
    this.discogsId = discogsId;
    this.dataQuality = dataQuality;
    this.subLabels = subLabels.orElse(new HashSet<>());
    this.parentLabel = parentLabel.orElse(null);
  }

  public Label() {}
}
