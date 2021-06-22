package graph.models.nodes

/**
  * Base trait for all node models.
  */
trait Node {

  /**
    * The Cypher label for the node, e.g. Artist, Label, etc.
    * Practically the label is the string version of the model class
    */
  val label: String
}
