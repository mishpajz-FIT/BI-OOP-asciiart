package registries

/**
  * Registry for items of type V indexed by keys of type K.
  * 
  * @tparam K type of keys
  * @tparam V type of items
  */
trait Registry[K, V] {

  /**
    * Item storage
    */
  protected var registry: Map[K, V] = Map.empty

  /**
    * Register an item with a key.
    * 
    * An item with the same key is overwritten.
    * 
    * @param key key of item
    * @param item item to register
    */
  def register(key: K, item: V): Unit = registry += (key -> item)

  /**
    * Unregister an item with key.
    * 
    * @param key key of item to unregister
    */
  def unregister(key: K): Unit = registry -= key

  /**
    * Get an item with key.
    * 
    * @param key key of item
    * @return item
    */
  def get(key: K): Option[V] = registry.get(key)

  /**
    * Get all registrered items.
    * 
    * @return [[Iterable]] of all items
    */
  def list(): Iterable[K] = registry.keys
}
