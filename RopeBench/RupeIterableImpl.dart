#library('Rupe');


class RupeIterableImpl implements Iterable {
  
  String _text = "Something else";
  
  Iterator<String> iterator() {
    return _text.splitChars().iterator();
  }
  
}
