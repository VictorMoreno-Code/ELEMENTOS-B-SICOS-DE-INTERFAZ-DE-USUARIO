import 'package:flutter_test/flutter_test.dart';
import 'package:uivision_flutter/main.dart';

void main() {
  testWidgets('La app abre en la pantalla de inicio', (WidgetTester tester) async {
    await tester.pumpWidget(const AppUIvision());
    expect(find.text('UIvision Flutter'), findsWidgets);
  });
}
