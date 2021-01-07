package managehelpcontentpublisher.capi

object Path {

  def toManagePath(capiPath: String): String = capiPath.substring(capiPath.lastIndexOf('/') + 1)
}
