package graph.dataAccess;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Test;
import graph.models.Label;

import static org.assertj.core.api.Assertions.assertThat;

import static graph.dataAccess.Factories.LabelFactory;

public class LabelNeo4jApiTest extends Neo4jTest {
  private final LabelNeo4jApi dataAccess = new LabelNeo4jApi(
      () -> super.testSession
    );

  @Test
  public void testGetByDiscogsId() {
    Label label = LabelFactory();
    Long id = label.getDiscogsId();

    dataAccess.create(label);

    Optional<Label> result = dataAccess.getByDiscogsId(id);
    assertThat(result.isPresent());
    assertThat(result.get().getDiscogsId()).isEqualTo(id);
  }


  @Test
  public void testGetByDiscogsIdNotFound() {
    Label label = LabelFactory();
    Long id = label.getDiscogsId();

    dataAccess.create(label);

    Optional<Label> emptyResult = dataAccess.getByDiscogsId(id + 1);
    assertThat(emptyResult.isEmpty());
  }

  @Test
  public void testCreateLabel() {
    Label label = LabelFactory();
    Long id = label.getDiscogsId();
    String dataQuality = label.getDataQuality();

    dataAccess.create(label);

    Optional<Label> result = dataAccess.getByDiscogsId(id);
    assertThat(result.isPresent());

    Label res = result.get();
    assertThat(res.getDiscogsId()).isEqualTo(id);
    assertThat(res.getDataQuality()).isEqualTo(dataQuality);
  }

  @Test
  public void testCreateLabelWithSubLabels() {
    Label subLabel1 = LabelFactory();
    Label subLabel2 = LabelFactory();
    Label parentLabel = LabelFactory();
    Long subLabelId1 = subLabel1.getDiscogsId();
    Long subLabelId2 = subLabel2.getDiscogsId();
    Long parentLabelId = parentLabel.getDiscogsId();

    Label.createParentSubLabelRelationship(parentLabel, subLabel1);
    Label.createParentSubLabelRelationship(parentLabel, subLabel2);
    dataAccess.create(parentLabel);

    Label sub1 = dataAccess.getByDiscogsId(subLabelId1).get();
    Label sub2 = dataAccess.getByDiscogsId(subLabelId2).get();
    Label parent = dataAccess.getByDiscogsId(parentLabelId).get();

    assertThat(sub1.getParentLabel().get().getDiscogsId()).isEqualTo(parentLabelId);
    assertThat(sub2.getParentLabel().get().getDiscogsId()).isEqualTo(parentLabelId);
    Label[] subLabels = parent.getSubLabels().get().toArray(new Label[0]);
    List<Long> subLabelDiscogsIds = Arrays.stream(subLabels).map(s -> s.getDiscogsId()).collect(Collectors.toList());
    assertThat(subLabelDiscogsIds).contains(subLabelId1);
    assertThat(subLabelDiscogsIds).contains(subLabelId2);
  }
}
