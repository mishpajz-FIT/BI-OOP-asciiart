package registries

trait Registry[K, V] {
  protected var registry: Map[K, V] = Map.empty

  def register(key: K, item: V): Unit = registry += (key -> item)

  def unregister(key: K): Unit = registry -= key

  def get(key: K): Option[V] = registry.get(key)

  def list(): Iterable[K] = registry.keys
}
