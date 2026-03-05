import 'package:flutter/material.dart';
import 'package:battery_plus/battery_plus.dart';

class BatteryWidget extends StatefulWidget {
  const BatteryWidget({super.key});

  @override
  State<BatteryWidget> createState() => _BatteryWidgetState();
}

class _BatteryWidgetState extends State<BatteryWidget> {
  final Battery _battery = Battery();
  int _batteryLevel = 100;

  @override
  void initState() {
    super.initState();
    _updateBatteryLevel();
  }

  Future<void> _updateBatteryLevel() async {
    final level = await _battery.batteryLevel;
    if (mounted) {
      setState(() {
        _batteryLevel = level;
      });
    }
  }

  IconData _getBatteryIcon() {
    if (_batteryLevel >= 90) return Icons.battery_full;
    if (_batteryLevel >= 70) return Icons.battery_6_bar;
    if (_batteryLevel >= 50) return Icons.battery_5_bar;
    if (_batteryLevel >= 30) return Icons.battery_3_bar;
    if (_batteryLevel >= 15) return Icons.battery_2_bar;
    return Icons.battery_alert;
  }

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        Icon(_getBatteryIcon(), size: 24),
        const SizedBox(width: 6),
        Text(
          '$_batteryLevel%',
          style: Theme.of(context)
              .textTheme
              .bodyMedium
              ?.copyWith(fontSize: 16),
        ),
      ],
    );
  }
}
